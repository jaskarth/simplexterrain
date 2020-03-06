package supercoder79.simplexterrain.world.gen;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

import it.unimi.dsi.fastutil.ints.IntRBTreeSet;
import it.unimi.dsi.fastutil.ints.IntSortedSet;
import net.minecraft.block.Blocks;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.noise.NoiseSampler;
import net.minecraft.util.math.noise.OctavePerlinNoiseSampler;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.IWorld;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ProtoChunk;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.OverworldChunkGeneratorConfig;
import supercoder79.simplexterrain.SimplexTerrain;
import supercoder79.simplexterrain.api.Heightmap;
import supercoder79.simplexterrain.api.noise.Noise;
import supercoder79.simplexterrain.api.noisemodifier.NoiseModifier;
import supercoder79.simplexterrain.api.noise.OctaveNoiseSampler;
import supercoder79.simplexterrain.api.postprocess.TerrainPostProcessor;
import supercoder79.simplexterrain.noise.NoiseMath;
import supercoder79.simplexterrain.noise.gradient.OpenSimplexNoise;
import supercoder79.simplexterrain.world.postprocessor.PostProcessors;


public class SimplexChunkGenerator extends ChunkGenerator<OverworldChunkGeneratorConfig> implements Heightmap {
	private static final ChunkRandom reuseableRandom = new ChunkRandom();

	public static SimplexChunkGenerator THIS;

	public final OctaveNoiseSampler newNoise;
	private final OpenSimplexNoise newNoise2;
	private final OpenSimplexNoise newNoise3;
	private final ChunkRandom random;

	private NoiseSampler surfaceDepthNoise;

	private ConcurrentHashMap<Long, int[]> noiseCache = new ConcurrentHashMap<>();

	private static ArrayList<TerrainPostProcessor> noisePostProcesors = new ArrayList<>();
	private static ArrayList<TerrainPostProcessor> carverPostProcesors = new ArrayList<>();
	private static ArrayList<TerrainPostProcessor> featurePostProcesors = new ArrayList<>();

	private static Map<Biome, Double> biome2FalloffMap = new HashMap<>();

	static {
		// Ocean biomes should not have 3d modification
		biome2FalloffMap.put(Biomes.OCEAN, 0.0);
		biome2FalloffMap.put(Biomes.COLD_OCEAN, 0.0);
		biome2FalloffMap.put(Biomes.FROZEN_OCEAN, 0.0);
		biome2FalloffMap.put(Biomes.LUKEWARM_OCEAN, 0.0);
		biome2FalloffMap.put(Biomes.WARM_OCEAN, 0.0);
		biome2FalloffMap.put(Biomes.DEEP_OCEAN, 0.0);
		biome2FalloffMap.put(Biomes.DEEP_COLD_OCEAN, 0.0);
		biome2FalloffMap.put(Biomes.DEEP_FROZEN_OCEAN, 0.0);
		biome2FalloffMap.put(Biomes.DEEP_LUKEWARM_OCEAN, 0.0);
		biome2FalloffMap.put(Biomes.DEEP_WARM_OCEAN, 0.0);

		biome2FalloffMap.put(Biomes.PLAINS, 7.5);
		biome2FalloffMap.put(Biomes.JUNGLE, 7.5);
		biome2FalloffMap.put(Biomes.BADLANDS, 12.5);
		biome2FalloffMap.put(Biomes.SHATTERED_SAVANNA_PLATEAU, 20.0);
	}

	private static Map<Biome, Double> biome2ThresholdMap = new HashMap<>();

	static {
		biome2ThresholdMap.put(Biomes.PLAINS, 0.15);
		biome2ThresholdMap.put(Biomes.JUNGLE, 0.14);
		biome2ThresholdMap.put(Biomes.FOREST, 0.175);
		biome2ThresholdMap.put(Biomes.BADLANDS, 0.1);
		biome2ThresholdMap.put(Biomes.SWAMP, 0.125);
		biome2ThresholdMap.put(Biomes.SHATTERED_SAVANNA_PLATEAU, 0.025);

		// oceans should never generate 3d noise part 2
		biome2FalloffMap.put(Biomes.OCEAN, 1.5);
		biome2FalloffMap.put(Biomes.COLD_OCEAN, 1.5);
		biome2FalloffMap.put(Biomes.FROZEN_OCEAN, 1.5);
		biome2FalloffMap.put(Biomes.LUKEWARM_OCEAN, 1.5);
		biome2FalloffMap.put(Biomes.WARM_OCEAN, 1.5);
		biome2FalloffMap.put(Biomes.DEEP_OCEAN, 1.5);
		biome2FalloffMap.put(Biomes.DEEP_COLD_OCEAN, 1.5);
		biome2FalloffMap.put(Biomes.DEEP_FROZEN_OCEAN, 1.5);
		biome2FalloffMap.put(Biomes.DEEP_LUKEWARM_OCEAN, 1.5);
		biome2FalloffMap.put(Biomes.DEEP_WARM_OCEAN, 1.5);
	}

	private final CompletableFuture[] futures;

	public SimplexChunkGenerator(IWorld world, BiomeSource biomeSource, OverworldChunkGeneratorConfig config) {
		super(world, biomeSource, config);
		this.random = new ChunkRandom(world.getSeed());

		Class<? extends Noise> noiseClass = SimplexTerrain.CONFIG.noiseGenerator.noiseClass;

		newNoise = new OctaveNoiseSampler<>(noiseClass, this.random, 4, 1024, 256+128, -128);
		newNoise2 = new OpenSimplexNoise(world.getSeed() - 30);
		newNoise3 = new OpenSimplexNoise(world.getSeed() + 30);

		futures = new CompletableFuture[SimplexTerrain.CONFIG.noiseGenerationThreads];

		if (biomeSource instanceof SimplexBiomeSource) {
			((SimplexBiomeSource)(this.biomeSource)).setHeightmap(this);
		}

		//TODO: remove this reflection fuckery
		try {
			Constructor<OctavePerlinNoiseSampler> constructor = OctavePerlinNoiseSampler.class.getDeclaredConstructor(ChunkRandom.class, IntSortedSet.class);
			constructor.setAccessible(true);
			surfaceDepthNoise = constructor.newInstance(random, new IntRBTreeSet(IntStream.rangeClosed(-3, 0).toArray()));
		} catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
			e.printStackTrace();
		}

		noisePostProcesors.forEach(postProcessor -> postProcessor.init(this.seed));
		carverPostProcesors.forEach(postProcessor -> postProcessor.init(this.seed));
		featurePostProcesors.forEach(postProcessor -> postProcessor.init(this.seed));
		noiseModifiers.forEach(noiseModifier -> noiseModifier.init(this.seed, reuseableRandom));

		THIS = this;
	}

	public static void addTerrainPostProcessor(TerrainPostProcessor postProcessor) {
		switch (postProcessor.getTarget()) {
			case NOISE:
				noisePostProcesors.add(postProcessor);
				break;
			case CARVERS:
				carverPostProcesors.add(postProcessor);
				break;
			case FEATURES:
				featurePostProcesors.add(postProcessor);
				break;
		}
	}

	private static final Collection<NoiseModifier> noiseModifiers = new ArrayList<>();

	public static void addNoiseModifier(NoiseModifier noiseModifier) {
		noiseModifiers.add(noiseModifier);
	}

	@Override
	public int getSpawnHeight() {
		return this.getSeaLevel() + 1;
	}

	public void populateEntities(ChunkRegion region) {
		int i = region.getCenterChunkX();
		int j = region.getCenterChunkZ();
		Biome biome = region.getBiome((new ChunkPos(i, j)).getCenterBlockPos());
		ChunkRandom chunkRandom = new ChunkRandom();
		chunkRandom.setSeed(region.getSeed(), i << 4, j << 4);
		SpawnHelper.populateEntities(region, biome, i, j, chunkRandom);
	}

	@Override
	public void populateBiomes(Chunk chunk) {
		ChunkPos chunkPos = chunk.getPos();
		((ProtoChunk)chunk).method_22405(SimplexBiomeArray.makeNewBiomeArray(chunkPos, (SimplexBiomeSource) this.biomeSource));
	}

	@Override
	public void populateNoise(IWorld iWorld, Chunk chunk) {
		BlockPos.Mutable pos = new BlockPos.Mutable();

		int[] requestedVals = getHeightsInChunk(chunk.getPos()); // attempt to retrieve the values from the cache

		for (int x = 0; x < 16; ++x) {
			pos.setX(x);

			for (int z = 0; z < 16; ++z) {
				pos.setZ(z);

				double falloff = 0;
				double threshold = 0;

				for (int x1 = -1; x1 <= 1; x1++) {
					for (int z1 = -1; z1 <= 1; z1++) {
						falloff += biome2FalloffMap.getOrDefault(
								biomeSource.getBiomeForNoiseGen((chunk.getPos().x*16) + (x + x1), getSeaLevel(), (chunk.getPos().z*16) + (z + z1)), 5.0);

						threshold += biome2ThresholdMap.getOrDefault(
								biomeSource.getBiomeForNoiseGen((chunk.getPos().x*16) + (x + x1), getSeaLevel(), (chunk.getPos().z*16) + (z + z1)), 0.2);
					}
				}

				falloff /= 9;
				threshold /= 9;

				int height = requestedVals[(x*16) + z];

				//Place guiding terrain
				for (int y = 0; y <= height; ++y) {
					pos.setY(y);
					chunk.setBlockState(pos, Blocks.STONE.getDefaultState(), false);
				}

				//3D modification (Bunes)
				for (int y = height; y < 256; y++) {
					if (y - height > 40) break;
					if (place3DNoise(chunk.getPos(), height - 1, x, y, z, falloff, threshold)) {
						pos.setY(y);
						chunk.setBlockState(pos, Blocks.STONE.getDefaultState(), false);
					}
				}

                // water placement
                for (int y = 0; y < getSeaLevel(); y++) {
                    pos.setY(y);
                    if (chunk.getBlockState(pos).isAir()) {
                        chunk.setBlockState(pos, Blocks.WATER.getDefaultState(), false);
                    }
                }
			}
		}
		noisePostProcesors.forEach(postProcessor -> postProcessor.process(world, new ChunkRandom(), chunk.getPos().x, chunk.getPos().z, this));
	}

	public boolean place3DNoise(ChunkPos pos, int height, int x, int y, int z, double falloff, double threshold) {
	    double coefficient = falloff / ((y - height) + 6); //height falloff

	    double noise1 = (newNoise2.sample(((pos.x*16) + x) / 90f, y / 30f, ((pos.z*16) + z) / 90f)*coefficient);
	    double noise2 = (newNoise3.sample(((pos.x*16) + x) / 90f, y / 30f, ((pos.z*16) + z) / 90f)*coefficient);

		return noise1 + noise2 > threshold;
    }

    public int getGuidingHeight(int x, int z) {
        return (int) NoiseMath.sigmoid(newNoise.sample(x, z));
    }

    @Override
	public int[] getHeightsInChunk(ChunkPos pos) {
		//return cached values
		int[] res = noiseCache.get(pos.toLong());
		if (res != null) return res;

		int[] vals = new int[256];

		if (SimplexTerrain.CONFIG.threadedNoiseGeneration) {
			for (int i = 0; i < SimplexTerrain.CONFIG.noiseGenerationThreads; i++) {
				int finalI = i;
				futures[i] = CompletableFuture.runAsync(() -> generateNoise(vals, pos, finalI * 16 / SimplexTerrain.CONFIG.noiseGenerationThreads, 16 / SimplexTerrain.CONFIG.noiseGenerationThreads),
						SimplexTerrain.globalThreadPool);
			}

			for (int i = 0; i < futures.length; i++) {
				futures[i].join();
			}

		} else {
			generateNoise(vals, pos, 0, 16); //generate all noise on the main thread
		}

		synchronized (this) {
			//cache the values
			if (noiseCache.size() > 1000) {
				noiseCache.clear();
			}
			noiseCache.put(pos.toLong(), vals);
		}

		return vals;
	}

	public void generateNoise(int[] noise, ChunkPos pos, int start, int size) {
		for (int x = start; x < start + size; x++) {
			for (int z = 0; z < 16; z++) {
				noise[(x*16) + z] = getHeight((pos.x * 16) + x, (pos.z * 16) + z);
			}
		}
	}

	@Override
	public int getHeightOnGround(int x, int z, net.minecraft.world.Heightmap.Type type) {
		return getHeight(x, z);
	}

	@Override
	public int getHeight(int x, int z) {
		return getGuidingHeight(x, z);
	}

	@Override
	public void buildSurface(ChunkRegion chunkRegion, Chunk chunk) {
		ChunkPos chunkPos = chunk.getPos();
		int i = chunkPos.x;
		int j = chunkPos.z;
		ChunkRandom chunkRandom = new ChunkRandom();
		chunkRandom.setSeed(i, j);
		ChunkPos chunkPos2 = chunk.getPos();
		int startX = chunkPos2.getStartX();
		int startZ = chunkPos2.getStartZ();
		BlockPos.Mutable mutable = new BlockPos.Mutable();

		for(int localX = 0; localX < 16; ++localX) {
			for(int localZ = 0; localZ < 16; ++localZ) {
				int x = startX + localX;
				int z = startZ + localZ;
				int height = chunk.sampleHeightmap(net.minecraft.world.Heightmap.Type.WORLD_SURFACE_WG, localX, localZ) + 1;
				double noise = this.surfaceDepthNoise.sample((double)x * 0.0625D, (double)z * 0.0625D, 0.0625D, (double)localX * 0.0625D);
				chunkRegion.getBiome(mutable.set(startX + localX, height, startZ + localZ)).buildSurface(chunkRandom, chunk, x, z, height, noise, this.getConfig().getDefaultBlock(), this.getConfig().getDefaultFluid(), this.getSeaLevel(), this.world.getSeed());
			}
		}

		this.buildBedrock(chunk, chunkRandom);
	}

	private void buildBedrock(Chunk chunk, Random random) {
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		int i = chunk.getPos().getStartX();
		int j = chunk.getPos().getStartZ();
		OverworldChunkGeneratorConfig chunkGeneratorConfig = this.getConfig();
		int k = chunkGeneratorConfig.getMinY();
		int l = chunkGeneratorConfig.getMaxY();
		Iterator<BlockPos> var9 = BlockPos.iterate(i, 0, j, i + 15, 0, j + 15).iterator();

		while(true) {
			BlockPos blockPos;
			int n;
			do {
				if (!var9.hasNext()) {
					return;
				}

				blockPos = var9.next();
				if (l > 0) {
					for(n = l; n >= l - 4; --n) {
						if (n >= l - random.nextInt(5)) {
							chunk.setBlockState(mutable.set(blockPos.getX(), n, blockPos.getZ()), Blocks.BEDROCK.getDefaultState(), false);
						}
					}
				}
			} while(k >= 256);

			for(n = k + 4; n >= k; --n) {
				if (n <= k + random.nextInt(5)) {
					chunk.setBlockState(mutable.set(blockPos.getX(), n, blockPos.getZ()), Blocks.BEDROCK.getDefaultState(), false);
				}
			}
		}
	}

	@Override
	public void generateFeatures(ChunkRegion region) {
		int chunkX = region.getCenterChunkX();
		int chunkZ = region.getCenterChunkZ();
		ChunkRandom rand = new ChunkRandom();
		rand.setSeed(chunkX, chunkZ);
		featurePostProcesors.forEach(postProcessor -> postProcessor.process(region, rand, chunkX, chunkZ, this));

		int i = region.getCenterChunkX();
		int j = region.getCenterChunkZ();
		int k = i * 16;
		int l = j * 16;
		BlockPos blockPos = new BlockPos(k, 0, l);
		Biome biome = this.getDecorationBiome(region.getBiomeAccess(), blockPos.add(8, 8, 8));
		ChunkRandom chunkRandom = new ChunkRandom();
		long seed = chunkRandom.setSeed(region.getSeed(), k, l);
		GenerationStep.Feature[] features = GenerationStep.Feature.values();
		int featureLength = features.length;

		for(int currentFeature = 0; currentFeature < featureLength; ++currentFeature) {
			GenerationStep.Feature feature = features[currentFeature];

			try {
				biome.generateFeatureStep(feature, this, region, seed, chunkRandom, blockPos);
			} catch (Exception exception) {
				CrashReport crashReport = CrashReport.create(exception, "Biome decoration");
				crashReport.addElement("Generation").add("CenterX", i).add("CenterZ", j).add("Step", feature).add("Seed", seed).add("Biome", Registry.BIOME.getId(biome));
				throw new CrashException(crashReport);
			}
		}
	}

	@Override
	public void carve(BiomeAccess biomeAccess, Chunk chunk, GenerationStep.Carver carver) {
		ChunkRandom chunkRandom = new ChunkRandom();
		ChunkPos chunkPos = chunk.getPos();
		int j = chunkPos.x;
		int k = chunkPos.z;
		Biome biome = this.getDecorationBiome(biomeAccess, chunkPos.getCenterBlockPos());
		BitSet bitSet = chunk.getCarvingMask(carver);
		if (!SimplexTerrain.CONFIG.generateVanillaCaves) return;

		for(int l = j - 8; l <= j + 8; ++l) {
			for(int m = k - 8; m <= k + 8; ++m) {
				List<ConfiguredCarver<?>> list = biome.getCarversForStep(carver);
				ListIterator listIterator = list.listIterator();

				while(listIterator.hasNext()) {
					int n = listIterator.nextIndex();
					ConfiguredCarver<?> configuredCarver = (ConfiguredCarver)listIterator.next();
					chunkRandom.setStructureSeed(this.seed + (long)n, l, m);
					if (configuredCarver.shouldCarve(chunkRandom, l, m)) {
						configuredCarver.carve(chunk, (blockPos) -> this.getDecorationBiome(biomeAccess, blockPos), chunkRandom, this.getSeaLevel(), l, m, j, k, bitSet);
					}
				}
			}
		}
	}

	// Irritatered
	public void carvePostProcessors(ChunkRegion world, Chunk chunk) {
		carverPostProcesors.forEach(postProcessor -> postProcessor.process(world, new ChunkRandom(), chunk.getPos().x, chunk.getPos().z, this));
	}

	@Override
	public int getSeaLevel() {
		return SimplexTerrain.CONFIG.seaLevel;
	}
}