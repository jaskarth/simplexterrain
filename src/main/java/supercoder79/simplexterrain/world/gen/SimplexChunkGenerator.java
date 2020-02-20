package supercoder79.simplexterrain.world.gen;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.noise.NoiseSampler;
import net.minecraft.util.math.noise.OctavePerlinNoiseSampler;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.IWorld;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ProtoChunk;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.OverworldChunkGeneratorConfig;
import supercoder79.simplexterrain.SimplexTerrain;
import supercoder79.simplexterrain.api.Heightmap;
import supercoder79.simplexterrain.api.cache.AbstractSampler;
import supercoder79.simplexterrain.api.cache.CacheSampler;
import supercoder79.simplexterrain.api.noise.Noise;
import supercoder79.simplexterrain.api.noise.NoiseModifier;
import supercoder79.simplexterrain.api.noise.OctaveNoiseSampler;
import supercoder79.simplexterrain.api.postprocess.TerrainPostProcessor;


public class SimplexChunkGenerator extends ChunkGenerator<OverworldChunkGeneratorConfig> implements Heightmap {
	private static final ChunkRandom reuseableRandom = new ChunkRandom();

	private final OctaveNoiseSampler heightNoise;
	private final OctaveNoiseSampler detailNoise;
	private final OctaveNoiseSampler scaleNoise;

	private final AbstractSampler scaleCache;
	private final AbstractSampler heightCache;

	private final ChunkRandom random;

	private NoiseSampler surfaceDepthNoise;

	private ConcurrentHashMap<Long, int[]> noiseCache = new ConcurrentHashMap<>();

	private final CompletableFuture[] futures;

	public SimplexChunkGenerator(IWorld world, BiomeSource biomeSource, OverworldChunkGeneratorConfig config) {
		super(world, biomeSource, config);
		this.random = new ChunkRandom(world.getSeed());

		double amplitude = Math.pow(2, SimplexTerrain.CONFIG.baseOctaveAmount);

		Class<? extends Noise> noiseClass = SimplexTerrain.CONFIG.noiseGenerator.noiseClass;
		heightNoise = new OctaveNoiseSampler<>(noiseClass, this.random, SimplexTerrain.CONFIG.baseOctaveAmount, SimplexTerrain.CONFIG.baseNoiseFrequencyCoefficient * amplitude, amplitude, amplitude);
		detailNoise = new OctaveNoiseSampler<>(noiseClass, this.random, SimplexTerrain.CONFIG.detailOctaveAmount, SimplexTerrain.CONFIG.detailFrequency, SimplexTerrain.CONFIG.detailAmplitudeHigh, SimplexTerrain.CONFIG.detailAmplitudeLow);
		scaleNoise = new OctaveNoiseSampler<>(noiseClass, this.random, SimplexTerrain.CONFIG.scaleOctaveAmount, Math.pow(2, SimplexTerrain.CONFIG.scaleFrequencyExponent), SimplexTerrain.CONFIG.scaleAmplitudeHigh, SimplexTerrain.CONFIG.scaleAmplitudeLow);

		scaleCache = CacheSampler.makeCacheSampler(scaleNoise);
		heightCache = CacheSampler.makeCacheSampler(heightNoise);

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

		postProcessors.forEach(postProcessor -> postProcessor.init(this.seed));
		noiseModifiers.forEach(noiseModifier -> noiseModifier.init(this.seed, reuseableRandom));
	}

	private static final Collection<TerrainPostProcessor> postProcessors = new ArrayList<>();

	public static void addTerrainPostProcessor(TerrainPostProcessor postProcessor) {
		postProcessors.add(postProcessor);
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
		((ProtoChunk)chunk).method_22405(SimplexBiomeArray.makeNewBiomeArray(chunkPos, this.biomeSource));
	}

	@Override
	public void populateNoise(IWorld iWorld, Chunk chunk) {
		BlockPos.Mutable posMutable = new BlockPos.Mutable();

		ChunkPos pos = chunk.getPos();

		int[] requestedVals = getHeightsInChunk(pos); // attempt to retrieve the values from the cache

		for (int x = 0; x < 16; ++x) {
			posMutable.setX(x);

			for (int z = 0; z < 16; ++z) {
				posMutable.setZ(z);

				for (int y = 0; y < 256; ++y) {
					posMutable.setY(y);
					double height = requestedVals[(x*16) + z];
					if (height >= y) {
						chunk.setBlockState(posMutable, Blocks.STONE.getDefaultState(), false);
					} else if (y < getSeaLevel()) {
						chunk.setBlockState(posMutable, Blocks.WATER.getDefaultState(), false);
					}
					//TODO: see if this actually improves performance
					if (y > height && y > getSeaLevel()) break;
				}
			}
		}
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
			if (noiseCache.size() > 500) {
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

	private double sigmoid(double val) {
		return 256 / (Math.exp(8/3f - val/48) + 1);
	}

	private static double fade(double value) {
		return value * value * (3 - (value * 2));
	}

	@Override
	public int getHeightOnGround(int x, int z, net.minecraft.world.Heightmap.Type type) {
		return getHeight(x, z);
	}

	@Override
	public int getHeight(int x, int z) {
		int xLow = ((x >> 2) << 2);
		int zLow = ((z >> 2) << 2);
		int xUpper = xLow + 4;
		int zUpper = zLow + 4;

		double xProgress = (double) (x - xLow) * 0.25;
		double zProgress = (double) (z - zLow) * 0.25;

		xProgress = fade(xProgress);
		zProgress = fade(zProgress);

		final double[] samples = new double[4];
		samples[0] = sampleNoise(xLow, zLow);
		samples[1] = sampleNoise(xUpper, zLow);
		samples[2] = sampleNoise(xLow, zUpper);
		samples[3] = sampleNoise(xUpper, zUpper);

		double sample = MathHelper.lerp(zProgress,
				MathHelper.lerp(xProgress, samples[0], samples[1]),
				MathHelper.lerp(xProgress, samples[2], samples[3]));

		double detail = 0;
		if (SimplexTerrain.CONFIG.addDetailNoise) {
			detail = sampleDetail(x, z);
		}

		return (int) (sigmoid((sample + detail)));
	}

	private double sampleNoise(int x, int z) {
		// Sample + shape and average
		double noise = sampleNoiseBase(x, z);
		noise += sampleNoiseBase(x + 4, z);
		noise += sampleNoiseBase(x - 4, z);
		noise += sampleNoiseBase(x, z + 4);
		noise += sampleNoiseBase(x, z - 4);
		noise *= 0.2;

		noise += SimplexTerrain.CONFIG.baseHeight;

		return noise;
	}

	private double sampleNoiseBase(int x, int z) {
//		System.out.println("sampling: " + x + ", " + z);
		// These values should use caches for efficiency but that takes effort
		double amplitudeSample = scaleCache.sample(x, z) + SimplexTerrain.CONFIG.scaleAmplitudeLow; // change range to have a minimum value of 0.0
		double noise = this.heightCache.sampleCustom(x, z, SimplexTerrain.CONFIG.baseNoiseSamplingFrequency, amplitudeSample, SimplexTerrain.CONFIG.baseOctaveAmount);

		for(NoiseModifier modifier : noiseModifiers) {
			noise = modifier.modify(x, z, noise, amplitudeSample);
		}

		return noise;
	}

	private double sampleDetail(int x, int z) {
		double sample = detailNoise.sample(x, z);
		if (sample < SimplexTerrain.CONFIG.detailNoiseThreshold) {
			if (scaleCache.sample(x, z) < SimplexTerrain.CONFIG.scaleNoiseThreshold) {
				sample = 0;
			}
		}
		return sample;
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
		postProcessors.forEach(postProcessor -> postProcessor.process(region, rand, chunkX, chunkZ, this));

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
	public int getSeaLevel() {
		return SimplexTerrain.CONFIG.seaLevel;
	}
}