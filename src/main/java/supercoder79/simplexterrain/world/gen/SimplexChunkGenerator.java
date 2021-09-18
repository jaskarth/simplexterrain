package supercoder79.simplexterrain.world.gen;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.structure.JigsawJunction;
import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.util.Util;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.math.*;
import net.minecraft.util.math.noise.NoiseSampler;
import net.minecraft.util.math.noise.OctaveSimplexNoiseSampler;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeAccess;
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
import net.minecraft.world.gen.feature.StructureFeature;
import supercoder79.simplexterrain.SimplexTerrain;
import supercoder79.simplexterrain.api.BackingBiomeSource;
import supercoder79.simplexterrain.api.Heightmap;
import supercoder79.simplexterrain.api.noise.Noise;
import supercoder79.simplexterrain.api.noisemodifier.NoiseModifier;
import supercoder79.simplexterrain.api.noise.OctaveNoiseSampler;
import supercoder79.simplexterrain.api.postprocess.TerrainPostProcessor;
import supercoder79.simplexterrain.noise.NoiseMath;
import supercoder79.simplexterrain.world.BiomeData;
import supercoder79.simplexterrain.world.blend.CachingBlender;
import supercoder79.simplexterrain.world.blend.LinkedBiomeWeightMap;
import supercoder79.simplexterrain.world.noisetype.*;


public class SimplexChunkGenerator extends ChunkGenerator implements Heightmap {
	public static final Codec<SimplexChunkGenerator> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
			BiomeSource.CODEC.fieldOf("biome_source").forGetter((generator) -> generator.biomeSource),
			Codec.LONG.fieldOf("seed").stable().forGetter((generator) -> generator.seed))
			.apply(instance, instance.stable(SimplexChunkGenerator::new)));
	public static SimplexChunkGenerator THIS;

	private static final float[] NOISE_WEIGHT_TABLE = Util.make(new float[9 * 9], (array) -> {
		for(int x = -4; x <= 4; ++x) {
			for(int z = -4; z <= 4; ++z) {
				float f = 10.0F / MathHelper.sqrt((float)(x * x + z * z) + 0.2F);
				array[x + 4 + (z + 4) * 9] = f;
			}
		}
	});

	public final OctaveNoiseSampler<? extends Noise> baseNoise;

	private NoiseSampler surfaceDepthNoise;

	private final ThreadLocal<Long2ObjectLinkedOpenHashMap<int[]>> noiseCache = new ThreadLocal<>();

	private static final ArrayList<TerrainPostProcessor> noisePostProcesors = new ArrayList<>();
	private static final ArrayList<TerrainPostProcessor> carverPostProcesors = new ArrayList<>();
	private static final ArrayList<TerrainPostProcessor> featurePostProcesors = new ArrayList<>();

	private final CompletableFuture<Void>[] futures;

	private final long seed;
	private final BiomeSource biomeSource;
	private final ContinentGenerator continentGenerator;

	private final BackingBiomeSource backing;

	private final CachingBlender blender;


	public SimplexChunkGenerator(BiomeSource biomeSource, long seed) {
		super(biomeSource, new StructuresConfig(true));

		init(seed);
		ChunkRandom random = new ChunkRandom(seed);
		this.biomeSource = biomeSource;

		if (!(this.biomeSource instanceof BackingBiomeSource)) {
			throw new IllegalStateException("Simplex terrain biome source must implement BackingBiomeSource");
		}

		this.backing = (BackingBiomeSource) this.biomeSource;

		Class<? extends Noise> noiseClass = SimplexTerrain.CONFIG.noiseGenerator.noiseClass;

		baseNoise = new OctaveNoiseSampler<>(noiseClass, random, SimplexTerrain.CONFIG.mainOctaveAmount, SimplexTerrain.CONFIG.mainFrequency, SimplexTerrain.CONFIG.mainAmplitudeHigh, SimplexTerrain.CONFIG.mainAmplitudeLow);

		futures = new CompletableFuture[SimplexTerrain.CONFIG.noiseGenerationThreads];

		this.continentGenerator = new ContinentGenerator(seed, this.getSeaLevel());
		this.blender = new CachingBlender(0.04, 24, 16);

		if (biomeSource instanceof SimplexBiomeSource) {
			SimplexBiomeSource source = (SimplexBiomeSource)biomeSource;
			source.setHeightmap(this);
			source.setContinentHeightmap(continentGenerator);
		}

		this.surfaceDepthNoise = new OctaveSimplexNoiseSampler(random, IntStream.rangeClosed(-3, 0));

		this.seed = seed;

		//TODO remove
		THIS = this;
	}

	// public for usage in MixinMinecraftServer#fixDumbServerCrash
	public static void init(long seed) {
		noisePostProcesors.forEach(postProcessor -> postProcessor.init(seed));
		carverPostProcesors.forEach(postProcessor -> postProcessor.init(seed));
		featurePostProcesors.forEach(postProcessor -> postProcessor.init(seed));
		noiseModifiers.forEach(noiseModifier -> noiseModifier.init(seed));
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
		int chunkX = region.getCenterChunkX();
		int chunkZ = region.getCenterChunkZ();
		region.getChunk(chunkX, chunkZ).getSectionArray();

		Biome biome = region.getBiome((new ChunkPos(chunkX, chunkZ)).getStartPos());
		ChunkRandom chunkRandom = new ChunkRandom();
		chunkRandom.setPopulationSeed(region.getSeed(), chunkX << 4, chunkZ << 4);
		SpawnHelper.populateEntities(region, biome, chunkX, chunkZ, chunkRandom);
	}

	@Override
	protected Codec<? extends ChunkGenerator> getCodec() {
		return CODEC;
	}

	@Override
	public ChunkGenerator withSeed(long l) {
		return new SimplexChunkGenerator(this.biomeSource.withSeed(l), l);
	}


	@Override
	public void populateNoise(WorldAccess world, StructureAccessor accessor, Chunk chunk) {
		ObjectList<StructurePiece> pieces = new ObjectArrayList(10);
		ObjectList<JigsawJunction> junctions = new ObjectArrayList(32);
		findStructures(chunk, accessor, pieces, junctions);

		BlockPos.Mutable pos = new BlockPos.Mutable();
		int[] requestedVals = getHeightsInChunk(chunk.getPos()); // attempt to retrieve the values from the cache

		int chunkStartX = chunk.getPos().getStartX();
		int chunkStartZ = chunk.getPos().getStartZ();

		for (int x = 0; x < 16; ++x) {
			pos.setX(x);

			for (int z = 0; z < 16; ++z) {
				pos.setZ(z);

				int height = requestedVals[(x * 16) + z];

				// TODO: needs to be refactored to double instead of int
				for (StructurePiece piece : pieces) {
					BlockBox box = piece.getBoundingBox();
					if (box.intersectsXZ(chunkStartX + x, chunkStartZ + z, chunkStartX + x, chunkStartZ + z)) {
						height = box.minY;
					} else if (box.intersectsXZ(chunkStartX + x - 8, chunkStartZ + z - 8, chunkStartX + x + 8, chunkStartZ + z + 8)) {
						double dx = Math.max(0, Math.max(box.minX - (chunkStartX + x), (chunkStartX + x) - box.maxX)) / 8.0;
						double dz = Math.max(0, Math.max(box.minZ - (chunkStartZ + z), (chunkStartZ + z) - box.maxZ)) / 8.0;
						double rad = dx * dx + dz * dz;

						double falloff = rad >= 1 ? 0 : (1 - rad) * (1 - rad);

						height = (int) MathHelper.lerp(falloff, height, box.minY);
					}
				}

				int genHeight = Math.max(getSeaLevel(), height);
				for (int y = 0; y < genHeight; ++y) {
					pos.setY(y);
					if (y > height) {
						chunk.setBlockState(pos, Blocks.WATER.getDefaultState(), false);
					} else {
						chunk.setBlockState(pos, Blocks.STONE.getDefaultState(), false);
					}

				}
			}
		}

		noisePostProcesors.forEach(postProcessor -> postProcessor.process(world, new ChunkRandom(), chunk.getPos().x, chunk.getPos().z, this));
	}

	private void findStructures(Chunk chunk, StructureAccessor accessor, ObjectList<StructurePiece> pieces, ObjectList<JigsawJunction> junctions) {
		ChunkPos chunkPos = chunk.getPos();
		int i = chunkPos.x;
		int j = chunkPos.z;
		int k = ChunkSectionPos.getBlockCoord(i);
		int l = ChunkSectionPos.getBlockCoord(j);
		Iterator var11 = StructureFeature.JIGSAW_STRUCTURES.iterator();

		while(var11.hasNext()) {
			StructureFeature<?> structureFeature = (StructureFeature)var11.next();
			accessor.getStructuresWithChildren(ChunkSectionPos.from(chunkPos, 0), structureFeature).forEach((start) -> {
				Iterator var6 = start.getChildren().iterator();

				while(true) {
					while(true) {
						StructurePiece structurePiece;
						do {
							if (!var6.hasNext()) {
								return;
							}

							structurePiece = (StructurePiece)var6.next();
						} while(!structurePiece.intersectsChunk(chunkPos, 12));

						if (structurePiece instanceof PoolStructurePiece) {
							PoolStructurePiece poolStructurePiece = (PoolStructurePiece)structurePiece;
							StructurePool.Projection projection = poolStructurePiece.getPoolElement().getProjection();
							if (projection == StructurePool.Projection.RIGID) {
								pieces.add(poolStructurePiece);
							}

							Iterator var10 = poolStructurePiece.getJunctions().iterator();

							while(var10.hasNext()) {
								JigsawJunction jigsawJunction = (JigsawJunction)var10.next();
								int kx = jigsawJunction.getSourceX();
								int lx = jigsawJunction.getSourceZ();
								if (kx > k - 12 && lx > l - 12 && kx < k + 15 + 12 && lx < l + 15 + 12) {
									junctions.add(jigsawJunction);
								}
							}
						} else {
							pieces.add(structurePiece);
						}
					}
				}
			});
		}
	}

    @Override
	public int[] getHeightsInChunk(ChunkPos pos) {
		if (noiseCache.get() == null) noiseCache.set(new Long2ObjectLinkedOpenHashMap<>());

		//return cached values
		int[] res = noiseCache.get().get(pos.toLong());
		if (res != null) return res;

		int[] vals = new int[256];

		// TODO: fix multithreading
//		if (SimplexTerrain.CONFIG.threadedNoiseGeneration) {
//			for (int i = 0; i < SimplexTerrain.CONFIG.noiseGenerationThreads; i++) {
//				int finalI = i;
//				futures[i] = CompletableFuture.runAsync(() -> generateNoise(vals, pos, finalI * 16 / SimplexTerrain.CONFIG.noiseGenerationThreads, 16 / SimplexTerrain.CONFIG.noiseGenerationThreads),
//						SimplexTerrain.globalThreadPool);
//			}
//
//			for (int i = 0; i < futures.length; i++) {
//				futures[i].join();
//			}
//
//		} else {
//			generateNoise(vals, pos, 0, 16); //generate all noise on the main thread
//		}

		generateNoise(vals, pos, 0, 16);

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
				noise[(x * 16) + z] = getHeight((pos.x * 16) + x, (pos.z * 16) + z);
			}
		}
	}

	private int getTypeAsInt(int x, int z) {
		return NoiseTypeHolder.IDS.get(NoiseTypeHolder.get(this.backing.getBacking(x, z)).get(x, z));
	}

	@Override
	public int getHeightOnGround(int x, int z, net.minecraft.world.Heightmap.Type type) {
		return getHeight(x, z);
	}

	@Override
	public int getHeight(int x, int z) {
		BiomeData data = new BiomeData();

		LinkedBiomeWeightMap weights = this.blender.getBlendForChunk(this.seed, (x >> 4) << 4, (z >> 4) << 4, (x0, z0) -> getTypeAsInt((int) x0, (int) z0));
		Map<NoiseType, double[]> typeWeights = new HashMap<>();
		do {
			typeWeights.put(NoiseTypeHolder.IDS_REVERSE.get(weights.getBiome()), weights.getWeights());
			weights = weights.getNext();
		} while (weights != null);

		double currentVal = this.continentGenerator.getHeight(x, z);

		currentVal = Math.min(currentVal, SimplexTerrain.CONFIG.seaLevel);

		double continent = 0;
		double weight = 0;

		for(int x1 = -12; x1 <= 12; x1++) {
		    for(int z1 = -12; z1 <= 12; z1++) {
				double weightHere = 1;

		    	if (this.continentGenerator.getHeight(x + x1, z + z1) >= SimplexTerrain.CONFIG.seaLevel) {
					continent += weightHere;
				}

				weight += weightHere;
		    }
		}

		continent /= weight;

		int idx = ((z & 15) * 16) + (x & 15);
		for (Map.Entry<NoiseType, double[]> entry : typeWeights.entrySet()) {
			// TODO: how can this happen??
			if (entry.getValue() == null || entry.getKey() == null) {
				continue;
			}

			double w = entry.getValue()[idx];
			currentVal += entry.getKey().modify(x, z, currentVal, w, data) * w * continent;
		}

		return (int) currentVal;
	}

	@Override
	public BiomeData getBiomeData(int x, int z) {
		BiomeData data = new BiomeData();

		double currentVal = baseNoise.sample(x, z);

		for (NoiseModifier modifier : noiseModifiers) {
			currentVal = modifier.modify(x, z, currentVal, data);
		}

		data.setHeight((int) (NoiseMath.sigmoid(currentVal)));

		return data;
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

		try {
			biome.generateFeatureStep(accesor, this, region, seed, chunkRandom, blockPos);
		} catch (Exception exception) {
			CrashReport crashReport = CrashReport.create(exception, "Biome decoration");
			crashReport.addElement("Generation").add("CenterX", i).add("CenterZ", j).add("Seed", seed).add("Biome", biome);
			throw new CrashException(crashReport);
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
		BitSet bitSet = ((ProtoChunk)chunk).getOrCreateCarvingMask(carver);

		for(int l = j - 8; l <= j + 8; ++l) {
			for(int m = k - 8; m <= k + 8; ++m) {
				List<Supplier<ConfiguredCarver<?>>> list = biome.getGenerationSettings().getCarversForStep(carver);
				ListIterator<Supplier<ConfiguredCarver<?>>> listIterator = list.listIterator();

				while(listIterator.hasNext()) {
					int n = listIterator.nextIndex();
					ConfiguredCarver<?> configuredCarver = listIterator.next().get();
					chunkRandom.setCarverSeed(seed + (long)n, l, m);
					if (configuredCarver.shouldCarve(chunkRandom, l, m)) {
						configuredCarver.carve(chunk, biomeAccess::getBiome, chunkRandom, this.getSeaLevel(), l, m, j, k, bitSet);
					}
				}
			}
		}

	}

	public void carvePostProcessors(ChunkRegion world, Chunk chunk) {
		carverPostProcesors.forEach(postProcessor -> postProcessor.process(world, new ChunkRandom(), chunk.getPos().x, chunk.getPos().z, this));
	}

	private static double getWeight(int x1, int z1) {
		return NOISE_WEIGHT_TABLE[x1 + 4 + (z1 + 4) * 9];
	}

	@Override
	public int getSeaLevel() {
		return SimplexTerrain.CONFIG.seaLevel;
	}

	@Override
	public int getHeight(int x, int z, net.minecraft.world.Heightmap.Type heightmapType) {
		return getHeight(x, z);
	}


	@Override
	public VerticalBlockSample getColumnSample(int x, int z) {
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

		return new VerticalBlockSample(0, array);
	}
}