package supercoder79.simplexterrain.world.gen;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.ints.IntRBTreeSet;
import it.unimi.dsi.fastutil.ints.IntSortedSet;
import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.noise.NoiseSampler;
import net.minecraft.util.math.noise.OctavePerlinNoiseSampler;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.BiomeArray;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ProtoChunk;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.StructuresConfig;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import supercoder79.simplexterrain.SimplexTerrain;
import supercoder79.simplexterrain.api.Heightmap;
import supercoder79.simplexterrain.api.noise.Noise;
import supercoder79.simplexterrain.api.noisemodifier.NoiseModifier;
import supercoder79.simplexterrain.api.noise.OctaveNoiseSampler;
import supercoder79.simplexterrain.api.postprocess.TerrainPostProcessor;
import supercoder79.simplexterrain.noise.NoiseMath;
import supercoder79.simplexterrain.noise.gradient.OpenSimplexNoise;


public class SimplexChunkGenerator extends ChunkGenerator implements Heightmap {
	public static final Codec<SimplexChunkGenerator> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
			BiomeSource.field_24713.fieldOf("biome_source").forGetter((generator) -> generator.biomeSource),
			Codec.LONG.fieldOf("seed").stable().forGetter((generator) -> generator.seed))
			.apply(instance, instance.stable(SimplexChunkGenerator::new)));
	public static SimplexChunkGenerator THIS;

	public final OctaveNoiseSampler<? extends Noise> baseNoise;
	private final OpenSimplexNoise newNoise2;
	private final OpenSimplexNoise newNoise3;

	private NoiseSampler surfaceDepthNoise;

	private final ThreadLocal<Long2ObjectLinkedOpenHashMap<int[]>> noiseCache = new ThreadLocal<>();

	private static final ArrayList<TerrainPostProcessor> noisePostProcesors = new ArrayList<>();
	private static final ArrayList<TerrainPostProcessor> carverPostProcesors = new ArrayList<>();
	private static final ArrayList<TerrainPostProcessor> featurePostProcesors = new ArrayList<>();

	private static final Map<Biome, Double> biome2FalloffMap = new HashMap<>();

	private static final Map<Biome, Double> biome2ThresholdMap = new HashMap<>();

	private final CompletableFuture<Void>[] futures;

	private final long seed;
	private final BiomeSource biomeSource;

	public SimplexChunkGenerator(BiomeSource biomeSource, long seed) {
		super(biomeSource, new StructuresConfig(true));

		noisePostProcesors.forEach(postProcessor -> postProcessor.init(seed));
		carverPostProcesors.forEach(postProcessor -> postProcessor.init(seed));
		featurePostProcesors.forEach(postProcessor -> postProcessor.init(seed));
		noiseModifiers.forEach(noiseModifier -> noiseModifier.init(seed));

		ChunkRandom random = new ChunkRandom(seed);
		this.biomeSource = biomeSource;

		Class<? extends Noise> noiseClass = SimplexTerrain.CONFIG.noiseGenerator.noiseClass;

		baseNoise = new OctaveNoiseSampler<>(noiseClass, random, SimplexTerrain.CONFIG.mainOctaveAmount, SimplexTerrain.CONFIG.mainFrequency, SimplexTerrain.CONFIG.mainAmplitudeHigh, SimplexTerrain.CONFIG.mainAmplitudeLow);
		newNoise2 = new OpenSimplexNoise(seed - 30);
		newNoise3 = new OpenSimplexNoise(seed + 30);

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

		this.seed = seed;

		//TODO remove
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
		chunkRandom.setPopulationSeed(region.getSeed(), i << 4, j << 4);
		SpawnHelper.populateEntities(region, biome, i, j, chunkRandom);
	}

	@Override
	protected Codec<? extends ChunkGenerator> method_28506() {
		return CODEC;
	}

	@Override
	public ChunkGenerator withSeed(long l) {
		return new SimplexChunkGenerator(new SimplexBiomeSource(l), l);
	}

	@Override
	public void populateBiomes(Chunk chunk) {
		ChunkPos chunkPos = chunk.getPos();
//		((ProtoChunk)chunk).setBiomes(SimplexBiomeArray.makeNewBiomeArray(chunkPos, this.biomeSource));
		((ProtoChunk)chunk).setBiomes(new BiomeArray(chunkPos, this.biomeSource));
	}

	@Override
	public void populateNoise(WorldAccess world, StructureAccessor accessor, Chunk chunk) {
		BlockPos.Mutable pos = new BlockPos.Mutable();
		int[] requestedVals = getHeightsInChunk(chunk.getPos()); // attempt to retrieve the values from the cache

		for (int x = 0; x < 16; ++x) {
			pos.setX(x);

			for (int z = 0; z < 16; ++z) {
				pos.setZ(z);

//				double falloff = 0;
//				double threshold = 0;
//
//				for (int x1 = -1; x1 <= 1; x1++) {
//					for (int z1 = -1; z1 <= 1; z1++) {
//						falloff += biome2FalloffMap.getOrDefault(
//								biomeSource.getBiomeForNoiseGen((chunk.getPos().x*16) + (x + x1), getSeaLevel(), (chunk.getPos().z*16) + (z + z1)), 5.0);
//
//						threshold += biome2ThresholdMap.getOrDefault(
//								biomeSource.getBiomeForNoiseGen((chunk.getPos().x*16) + (x + x1), getSeaLevel(), (chunk.getPos().z*16) + (z + z1)), 0.2);
//					}
//				}
//
//				falloff /= 9;
//				threshold /= 9;

				int height = requestedVals[(x*16) + z];

				//Place guiding terrain
				for (int y = 0; y <= height; ++y) {
					pos.setY(y);
					chunk.setBlockState(pos, Blocks.STONE.getDefaultState(), false);
				}


				//3D modification (Bunes)
//				for (int y = height; y < 256; y++) {
//					if (y - height > 40) break;
//					if (place3DNoise(chunk.getPos(), height - 1, x, y, z, falloff, threshold)) {
//						pos.setY(y);
//						chunk.setBlockState(pos, Blocks.STONE.getDefaultState(), false);
//					}
//				}

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

    @Override
	public int[] getHeightsInChunk(ChunkPos pos) {
		if (noiseCache.get() == null) noiseCache.set(new Long2ObjectLinkedOpenHashMap<>());

		//return cached values
		int[] res = noiseCache.get().get(pos.toLong());
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

		//cache the values
		if (noiseCache.get().size() > 16) {
			noiseCache.get().removeFirst();
		}
		noiseCache.get().put(pos.toLong(), vals);

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
		double currentVal = baseNoise.sample(x, z);

		for (NoiseModifier modifier : noiseModifiers) {
			currentVal = modifier.modify(x, z, currentVal);
		}

		return (int) (NoiseMath.sigmoid(currentVal));
	}

	@Override
	public void buildSurface(ChunkRegion chunkRegion, Chunk chunk) {
		ChunkPos chunkPos = chunk.getPos();
		int i = chunkPos.x;
		int j = chunkPos.z;
		ChunkRandom chunkRandom = new ChunkRandom();
		chunkRandom.setTerrainSeed(i, j);
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
				chunkRegion.getBiome(mutable.set(startX + localX, height, startZ + localZ)).buildSurface(chunkRandom, chunk, x, z, height, noise, Blocks.STONE.getDefaultState(), Blocks.WATER.getDefaultState(), this.getSeaLevel(), seed);
			}
		}

		this.buildBedrock(chunk, chunkRandom);
	}

	private void buildBedrock(Chunk chunk, Random random) {
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		int i = chunk.getPos().getStartX();
		int j = chunk.getPos().getStartZ();
		int k = 0; //bedrock floor y
		int l = 0; //bedrock ceiling y
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
	public void generateFeatures(ChunkRegion region, StructureAccessor accesor) {
		int chunkX = region.getCenterChunkX();
		int chunkZ = region.getCenterChunkZ();
		ChunkRandom rand = new ChunkRandom();
		//TODO: add world seed
		//update: i'm super fucking glad i didn't add the world seed here
		rand.setTerrainSeed(chunkX, chunkZ);
		featurePostProcesors.forEach(postProcessor -> postProcessor.process(region, rand, chunkX, chunkZ, this));

		int i = region.getCenterChunkX();
		int j = region.getCenterChunkZ();
		int k = i * 16;
		int l = j * 16;
		BlockPos blockPos = new BlockPos(k, 0, l);
		Biome biome = this.biomeSource.getBiomeForNoiseGen((i << 2) + 2, 2, (j << 2) + 2);
		ChunkRandom chunkRandom = new ChunkRandom();
		long seed = chunkRandom.setPopulationSeed(region.getSeed(), k, l);
		GenerationStep.Feature[] features = GenerationStep.Feature.values();
		int featureLength = features.length;

		for(int currentFeature = 0; currentFeature < featureLength; ++currentFeature) {
			GenerationStep.Feature feature = features[currentFeature];

			try {
				biome.generateFeatureStep(feature, accesor, this, region, seed, chunkRandom, blockPos);
			} catch (Exception exception) {
				CrashReport crashReport = CrashReport.create(exception, "Biome decoration");
				crashReport.addElement("Generation").add("CenterX", i).add("CenterZ", j).add("Step", feature).add("Seed", seed).add("Biome", Registry.BIOME.getId(biome));
				throw new CrashException(crashReport);
			}
		}
	}

	public void carve(long seed, BiomeAccess access, Chunk chunk, GenerationStep.Carver carver) {
		if (!SimplexTerrain.CONFIG.generateVanillaCaves) return;

		BiomeAccess biomeAccess = access.withSource(this.biomeSource);
		ChunkRandom chunkRandom = new ChunkRandom();
		ChunkPos chunkPos = chunk.getPos();
		int j = chunkPos.x;
		int k = chunkPos.z;
		Biome biome = this.biomeSource.getBiomeForNoiseGen(chunkPos.x << 2, 0, chunkPos.z << 2);
		BitSet bitSet = ((ProtoChunk)chunk).method_28510(carver);

		for(int l = j - 8; l <= j + 8; ++l) {
			for(int m = k - 8; m <= k + 8; ++m) {
				List<ConfiguredCarver<?>> list = biome.getCarversForStep(carver);
				ListIterator listIterator = list.listIterator();

				while(listIterator.hasNext()) {
					int n = listIterator.nextIndex();
					ConfiguredCarver<?> configuredCarver = (ConfiguredCarver)listIterator.next();
					chunkRandom.setCarverSeed(seed + (long)n, l, m);
					if (configuredCarver.shouldCarve(chunkRandom, l, m)) {
						configuredCarver.carve(chunk, biomeAccess::getBiome, chunkRandom, this.getSeaLevel(), l, m, j, k, bitSet);
					}
				}
			}
		}

	}

	// irritatered
	public void carvePostProcessors(ChunkRegion world, Chunk chunk) {
		carverPostProcesors.forEach(postProcessor -> postProcessor.process(world, new ChunkRandom(), chunk.getPos().x, chunk.getPos().z, this));
	}

	@Override
	public int getSeaLevel() {
		return SimplexTerrain.CONFIG.seaLevel;
	}

	//TODO: figure out what this bullshit does
	@Override
	public int getHeight(int x, int z, net.minecraft.world.Heightmap.Type heightmapType) {
		return getHeight(x, z);
	}

	//????????????????????????????
	@Override
	public BlockView getColumnSample(int x, int z) {
		int height = getHeight(x, z);
		BlockState[] array = new BlockState[256];
		for (int y = 255; y >= 0; y--) {
			if (y > height) {
				if (y > getSeaLevel()) {
					array[y] = Blocks.AIR.getDefaultState();
				} else {
					array[y] = Blocks.WATER.getDefaultState();
				}
			} else {
				array[y] = Blocks.STONE.getDefaultState();
			}
		}

		return new VerticalBlockSample(array);
	}

	//Too lazy to clean this shit up

	static {
		biome2ThresholdMap.put(Biomes.PLAINS, 0.15);
		biome2ThresholdMap.put(Biomes.JUNGLE, 0.14);
		biome2ThresholdMap.put(Biomes.FOREST, 0.175);
		biome2ThresholdMap.put(Biomes.BADLANDS, 0.1);
		biome2ThresholdMap.put(Biomes.SWAMP, 0.125);
		biome2ThresholdMap.put(Biomes.SHATTERED_SAVANNA_PLATEAU, 0.025);

		// oceans should never generate 3d noise part 2
		biome2ThresholdMap.put(Biomes.OCEAN, 0.0);
		biome2ThresholdMap.put(Biomes.COLD_OCEAN, 0.0);
		biome2ThresholdMap.put(Biomes.FROZEN_OCEAN, 0.0);
		biome2ThresholdMap.put(Biomes.LUKEWARM_OCEAN, 0.0);
		biome2ThresholdMap.put(Biomes.WARM_OCEAN, 0.0);
		biome2ThresholdMap.put(Biomes.DEEP_OCEAN, 0.0);
		biome2ThresholdMap.put(Biomes.DEEP_COLD_OCEAN, 0.0);
		biome2ThresholdMap.put(Biomes.DEEP_FROZEN_OCEAN, 0.0);
		biome2ThresholdMap.put(Biomes.DEEP_LUKEWARM_OCEAN, 0.0);
		biome2ThresholdMap.put(Biomes.DEEP_WARM_OCEAN, 0.0);

		// Ocean biomes should not have 3d modification
		biome2FalloffMap.put(Biomes.OCEAN, 1.0);
		biome2FalloffMap.put(Biomes.COLD_OCEAN, 1.0);
		biome2FalloffMap.put(Biomes.FROZEN_OCEAN, 1.0);
		biome2FalloffMap.put(Biomes.LUKEWARM_OCEAN, 1.0);
		biome2FalloffMap.put(Biomes.WARM_OCEAN, 1.0);
		biome2FalloffMap.put(Biomes.DEEP_OCEAN, 1.0);
		biome2FalloffMap.put(Biomes.DEEP_COLD_OCEAN, 1.0);
		biome2FalloffMap.put(Biomes.DEEP_FROZEN_OCEAN, 1.0);
		biome2FalloffMap.put(Biomes.DEEP_LUKEWARM_OCEAN, 1.0);
		biome2FalloffMap.put(Biomes.DEEP_WARM_OCEAN, 1.0);

		biome2FalloffMap.put(Biomes.PLAINS, 7.5);
		biome2FalloffMap.put(Biomes.JUNGLE, 7.5);
		biome2FalloffMap.put(Biomes.BADLANDS, 12.5);
		biome2FalloffMap.put(Biomes.SHATTERED_SAVANNA_PLATEAU, 20.0);
	}
}