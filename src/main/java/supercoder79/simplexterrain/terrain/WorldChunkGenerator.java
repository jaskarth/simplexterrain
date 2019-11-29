package supercoder79.simplexterrain.terrain;

import java.util.Iterator;
import java.util.Random;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.noise.NoiseSampler;
import net.minecraft.util.math.noise.OctavePerlinNoiseSampler;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.OverworldChunkGeneratorConfig;
import supercoder79.simplexterrain.api.Heightmap;
import supercoder79.simplexterrain.noise.OctaveOSNoiseSampler;

public class WorldChunkGenerator extends ChunkGenerator<OverworldChunkGeneratorConfig> implements Heightmap {
	public static OctaveOSNoiseSampler heightNoise;

	private final ChunkRandom random;
	private final NoiseSampler surfaceDepthNoise;

	public WorldChunkGenerator(IWorld world, BiomeSource biomeSource, OverworldChunkGeneratorConfig config) {
		super(world, biomeSource, config);
		this.random = new ChunkRandom(world.getSeed());
		
		double amplitude = Math.pow(2, 11);
		heightNoise = new OctaveOSNoiseSampler(this.random, 11, 0.4 * amplitude, amplitude, amplitude);

		((WorldBiomeSource)(this.biomeSource)).setHeightmap(this);
		
		this.surfaceDepthNoise = new OctavePerlinNoiseSampler(this.random, 4);
	}

	@Override
	public int getSpawnHeight() {
		return this.getSeaLevel() + 1;
	}

	@Override
	public void populateBiomes(Chunk chunk) {
		super.populateBiomes(chunk);
		//        ArrayList<Biome> biomeArray = new ArrayList<>(256);
		//        for (int x = 0; x < 16; ++x) {
		//            for (int z = 0; z < 16; ++z) {
		//                int height = (int) (NOISE_SAMPLERS[10].sample(chunk.getPos().x * 16 + x, chunk.getPos().z * 16 + z, true) * 0.1 + 100);
		//                if (height > 130)
		//                    biomeArray.add(Biomes.MOUNTAINS);
		//                else if (height > 100)
		//                    biomeArray.add(Biomes.MOUNTAIN_EDGE);
		//                else if (height > 63)
		//                    biomeArray.add(Biomes.FOREST);
		//                else
		//                    biomeArray.add(Biomes.OCEAN);
		//            }
		//        }
		//        Biome[] b = new Biome[256];
		//        b = biomeArray.toArray(b);
		//        chunk.setBiomeArray(b);
	}

	@Override
	public void populateNoise(IWorld iWorld, Chunk chunk) {
		BlockPos.Mutable posMutable = new BlockPos.Mutable();
		for (int x = 0; x < 16; ++x) {
			for (int z = 0; z < 16; ++z) {
				int height = getHeight(chunk.getPos().x*16 + x, chunk.getPos().z*16 + z);
				for (int y = 0; y < 256; ++y) {
					if (height >= y) {
						chunk.setBlockState(posMutable.set(x, y, z), Blocks.STONE.getDefaultState(), false);
					} else if (y < 63) {
						chunk.setBlockState(posMutable.set(x, y, z), Blocks.WATER.getDefaultState(), false);
					}
				}
			}
		}
	}

	@Override
	public int getHeightOnGround(int i, int j, net.minecraft.world.Heightmap.Type type) {
		return getHeight(i, j);
	}

	@Override
	public int getHeight(int x, int z) {
		return  (int) (heightNoise.sample(x, z) * 0.1 + 100);
	}

	public void buildSurface(Chunk chunk) {
		ChunkPos chunkPos = chunk.getPos();
		int i = chunkPos.x;
		int j = chunkPos.z;
		ChunkRandom chunkRandom = new ChunkRandom();
		chunkRandom.setSeed(i, j);
		ChunkPos chunkPos2 = chunk.getPos();
		int k = chunkPos2.getStartX();
		int l = chunkPos2.getStartZ();
		Biome[] biomes = chunk.getBiomeArray();

		for(int m = 0; m < 16; ++m) {
			for(int n = 0; n < 16; ++n) {
				int o = k + m;
				int p = l + n;
				int q = chunk.sampleHeightmap(net.minecraft.world.Heightmap.Type.WORLD_SURFACE_WG, m, n) + 1;
				double e = this.surfaceDepthNoise.sample((double)o * 0.0625D, (double)p * 0.0625D, 0.0625D, (double)m * 0.0625D);
				biomes[n * 16 + m].buildSurface(chunkRandom, chunk, o, p, q, e, this.getConfig().getDefaultBlock(), this.getConfig().getDefaultFluid(), this.getSeaLevel(), this.world.getSeed());
			}
		}

		this.buildBedrock(chunk, chunkRandom);
	}

	protected void buildBedrock(Chunk chunk, Random random) {
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

				blockPos = (BlockPos)var9.next();
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
}