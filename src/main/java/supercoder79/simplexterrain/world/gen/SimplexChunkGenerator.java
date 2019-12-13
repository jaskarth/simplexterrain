package supercoder79.simplexterrain.world.gen;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.LongFunction;
import java.util.stream.Stream;

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
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeArray;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ProtoChunk;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.OverworldChunkGeneratorConfig;
import org.apache.commons.lang3.ArrayUtils;
import supercoder79.simplexterrain.SimplexTerrain;
import supercoder79.simplexterrain.api.Heightmap;
import supercoder79.simplexterrain.api.noise.Noise;
import supercoder79.simplexterrain.api.noise.OctaveNoiseSampler;
import supercoder79.simplexterrain.api.postprocess.TerrainPostProcessor;


public class SimplexChunkGenerator extends ChunkGenerator<OverworldChunkGeneratorConfig> implements Heightmap {

	private final OctaveNoiseSampler heightNoise;
	private final OctaveNoiseSampler detailNoise;
	private final OctaveNoiseSampler scaleNoise;
	private final OctaveNoiseSampler peaksNoise;

	private final ChunkRandom random;
	private final NoiseSampler surfaceDepthNoise;
	private final Iterable<TerrainPostProcessor> terrainPostProcessors;

	private HashMap<ChunkPos, int[]> noiseCache = new HashMap<>();

	public SimplexChunkGenerator(IWorld world, BiomeSource biomeSource, OverworldChunkGeneratorConfig config) {
		super(world, biomeSource, config);
		this.random = new ChunkRandom(world.getSeed());

		double amplitude = Math.pow(2, SimplexTerrain.CONFIG.baseOctaveAmount);

		Class<? extends Noise> noiseClass = SimplexTerrain.CONFIG.noiseGenerator.noiseClass;
		heightNoise = new OctaveNoiseSampler<>(noiseClass, this.random, SimplexTerrain.CONFIG.baseOctaveAmount, SimplexTerrain.CONFIG.baseNoiseFrequencyCoefficient * amplitude, amplitude, amplitude);
		detailNoise = new OctaveNoiseSampler<>(noiseClass, this.random, SimplexTerrain.CONFIG.detailOctaveAmount, SimplexTerrain.CONFIG.detailFrequency, SimplexTerrain.CONFIG.detailAmplitudeHigh, SimplexTerrain.CONFIG.detailAmplitudeLow);
		scaleNoise = new OctaveNoiseSampler<>(noiseClass, this.random, SimplexTerrain.CONFIG.scaleOctaveAmount, Math.pow(2, SimplexTerrain.CONFIG.scaleFrequencyExponent), SimplexTerrain.CONFIG.scaleAmplitudeHigh, SimplexTerrain.CONFIG.scaleAmplitudeLow);
		peaksNoise = new OctaveNoiseSampler<>(noiseClass, this.random, SimplexTerrain.CONFIG.peaksOctaveAmount, SimplexTerrain.CONFIG.peaksFrequency, 1.0, 1.0);


		if (biomeSource instanceof SimplexBiomeSource) {
			((SimplexBiomeSource)(this.biomeSource)).setHeightmap(this);
		}

		this.surfaceDepthNoise = new OctavePerlinNoiseSampler(this.random, 4, 0);

		List<TerrainPostProcessor> postProcessors = new ArrayList<>();
		postProcessorFactories.forEach(factory -> postProcessors.add(factory.apply(this.seed)));
		terrainPostProcessors = postProcessors;
	}

	private static final Collection<LongFunction<TerrainPostProcessor>> postProcessorFactories = new ArrayList<>();

	public static void addTerrainPostProcessor(LongFunction<TerrainPostProcessor> factory) {
		postProcessorFactories.add(factory);
	}

	@Override
	public int getSpawnHeight() {
		return this.getSeaLevel() + 1;
	}

	//TODO: Fix this code
//	@Override
//	public void populateBiomes(Chunk chunk) {
//		if (biomeSource instanceof SimplexBiomeSource) {
//			ChunkPos chunkPos = chunk.getPos();
//			int[] e = getHeightInChunk(chunkPos);
//			Biome[] biomes = new Biome[256];
//
//			for (int x = 0; x < 16; x++) {
//				for (int z = 0; z < 16; z++) {
//					biomes[z * 16 + x] = ((SimplexBiomeSource)(biomeSource)).sampleBiomeWithMathTM(x, z, e[z*16 + x]);
//				}
//			}
//
//			((ProtoChunk) chunk).method_22405(new BiomeArray(biomes));
//		} else {
//			super.populateBiomes(chunk);
//		}
//	}

	@Override
	public void populateNoise(IWorld iWorld, Chunk chunk) {
		BlockPos.Mutable posMutable = new BlockPos.Mutable();

		ChunkPos pos = chunk.getPos();

		int[] requestedVals = getHeightInChunk(pos); //attempt to retrieve the values from the cache

		for (int x = 0; x < 16; ++x) {
			posMutable.setX(x);

			for (int z = 0; z < 16; ++z) {
				posMutable.setZ(z);

				for (int y = 0; y < 256; ++y) {
					posMutable.setY(y);
					double height = requestedVals[(x*16) + z];
					if (height >= y) {
						chunk.setBlockState(posMutable, Blocks.STONE.getDefaultState(), false);
					} else if (y < 63) {
						chunk.setBlockState(posMutable, Blocks.WATER.getDefaultState(), false);
					}
					//TODO: see if this actually improves performance
					if (y > height && y > 63) break;
				}
			}
		}
	}

	@Override
	public int[] getHeightInChunk(ChunkPos pos) {
		//return cached values
		int[] res = noiseCache.get(pos);
		if (res != null) return res;

		int[] vals = new int[256];
		if (SimplexTerrain.CONFIG.threadedNoiseGeneration) {
			CompletableFuture[] futures = new CompletableFuture[2];
			for (int i = 0; i < 2; i++) {
				int finalI = i;
				futures[i] = CompletableFuture.runAsync(() -> generateNoise(vals, pos, finalI * 8, 8));
			}

			for (int i = 0; i < futures.length; i++) {
				futures[i].join();
			}

		} else {
			generateNoise(vals, pos, 0, 16); //generate all noise on the main thread
		}

		//cache the values
		noiseCache.put(pos, vals);

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

	@Override
	public int getHeightOnGround(int x, int z, net.minecraft.world.Heightmap.Type type) {
		return getHeight(x, z);
	}

	@Override
	public int getHeight(int x, int z) {
		int subX = ((x >> 2) << 2);
		int subZ = ((z >> 2) << 2);
		int subXUpper = subX + 4;
		int subZUpper = subZ + 4;

		double xProgress = (double) (x - subX) / 4.0;
		double zProgress = (double) (z - subZ) / 4.0;

		final double[] samples = new double[4];
		samples[0] = sampleNoise(subX, subZ);
		samples[1] = sampleNoise(subXUpper, subZ);
		samples[2] = sampleNoise(subX, subZUpper);
		samples[3] = sampleNoise(subXUpper, subZUpper);

		double sample = MathHelper.lerp(zProgress,
				MathHelper.lerp(xProgress, samples[0], samples[1]),
				MathHelper.lerp(xProgress, samples[2], samples[3]));

		double detail = 0;
		if (SimplexTerrain.CONFIG.addDetailNoise) {
			detail = sampleDetail(x, z);
		}
		return (int)(sigmoid((sample + detail)));
	}

	private double sampleNoise(int x, int z) {
		double amplitudeSample = this.scaleNoise.sample(x, z) + SimplexTerrain.CONFIG.scaleAmplitudeLow; // change range to have a minimum value of 0.0
		return this.heightNoise.sampleCustom(x, z, SimplexTerrain.CONFIG.baseNoiseSamplingFrequency, amplitudeSample, amplitudeSample, SimplexTerrain.CONFIG.baseOctaveAmount)
				+ modifyPeaksNoise(this.peaksNoise.sample(x, z))
				+ SimplexTerrain.CONFIG.baseHeight;
	}

	private static double modifyPeaksNoise(double sample) {
		if (SimplexTerrain.CONFIG.addPeaksNoise) return 0;
		sample += SimplexTerrain.CONFIG.peaksSampleOffset;
		if (sample < 0) {
			return 0;
		} else {
			return sample * SimplexTerrain.CONFIG.peaksAmplitude;
		}
	}

	private double sampleDetail(int x, int z) {
		double sample = detailNoise.sample(x, z);
		if (sample < SimplexTerrain.CONFIG.detailNoiseThreshold) {
			if (scaleNoise.sample(x, z) < SimplexTerrain.CONFIG.scaleNoiseThreshold) {
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
		this.terrainPostProcessors.forEach(postProcessor -> postProcessor.postProcess(region, rand, chunkX, chunkZ, this));

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