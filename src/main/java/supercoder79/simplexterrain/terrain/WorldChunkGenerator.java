package supercoder79.simplexterrain.terrain;

import java.util.Iterator;
import java.util.Random;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.noise.NoiseSampler;
import net.minecraft.util.math.noise.OctavePerlinNoiseSampler;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeArray;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.OverworldChunkGeneratorConfig;
import supercoder79.simplexterrain.api.Heightmap;
import supercoder79.simplexterrain.noise.OctaveOpenSimplexNoise;
import supercoder79.simplexterrain.terrain.biomesource.WorldBiomeSource;

public class WorldChunkGenerator extends ChunkGenerator<OverworldChunkGeneratorConfig> implements Heightmap {
    private final OctaveOpenSimplexNoise heightNoise;
    private final OctaveOpenSimplexNoise detailNoise;
    private final OctaveOpenSimplexNoise scaleNoise;

    private final ChunkRandom random;
    private final NoiseSampler surfaceDepthNoise;

    public WorldChunkGenerator(IWorld world, BiomeSource biomeSource, OverworldChunkGeneratorConfig config) {
        super(world, biomeSource, config);
        this.random = new ChunkRandom(world.getSeed());

        double amplitude = Math.pow(2, 11);

        heightNoise = new OctaveOpenSimplexNoise(this.random, 11, 0.4 * amplitude, amplitude, amplitude);
        detailNoise = new OctaveOpenSimplexNoise(this.random, 4, 20, 2, 4);
        scaleNoise = new OctaveOpenSimplexNoise(this.random, 2, Math.pow(2, 10), 0.06, 0.06); // 0.06 * 2 = 0.12, maximum scale is 0.12 (default constant before noise was 0.1)

        ((WorldBiomeSource)(this.biomeSource)).setHeightmap(this);

        this.surfaceDepthNoise = new OctavePerlinNoiseSampler(this.random, 4, 0);
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

        int chunkX = chunk.getPos().x;
        int chunkZ = chunk.getPos().z;

        for (int x = 0; x < 16; ++x) {
            posMutable.setX(x);

            for (int z = 0; z < 16; ++z) {
                posMutable.setZ(z);
                int height = getHeight(chunkX * 16 + x, chunkZ * 16 + z);
//                System.out.println(chunkX+x);
//                System.out.println(chunkZ+z);
//                System.out.println(height);
//                System.out.println("***");

                for (int y = 0; y < 256; ++y) {
                    posMutable.setY(y);
                    if (y <= height) {
                        chunk.setBlockState(posMutable, Blocks.STONE.getDefaultState(), false);
                    } else if (y < 63) {
                        chunk.setBlockState(posMutable, Blocks.WATER.getDefaultState(), false);
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
        int subX = ((x >> 2) << 2);
        int subZ = ((z >> 2) << 2);
        int subXUpper = subX + 4;
        int subZUpper = subZ + 4;

        double xProgress = (double) (x - subX) / 4.0;
        double zProgress = (double) (z - subZ) / 4.0;

        double sampleNW = sampleNoise(subX, subZ);
        double sampleNE = sampleNoise(subXUpper, subZ);
        double sampleSW = sampleNoise(subX, subZUpper);
        double sampleSE = sampleNoise(subXUpper, subZUpper);

        double sample = MathHelper.lerp(zProgress,
                MathHelper.lerp(xProgress, sampleNW, sampleNE),
                MathHelper.lerp(xProgress, sampleSW, sampleSE));

        return (int) (sample + sampleDetail(x, z));
    }

    private double sampleNoise(int x, int z) {
        double amplitudeSample = scaleNoise.sample(x, z) + 0.06; // change range [-0.06, 0.06] to [0.0, 0.12]
        return heightNoise.sampleCustom(x, z, 1.0, amplitudeSample, amplitudeSample, 11) + 100;
    }

    private double sampleDetail(int x, int z) {
        double sample = detailNoise.sample(x, z);
        if (sample < 0.0) {
            if (scaleNoise.sample(x, z) < -0.02) {
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
        BiomeArray biomes = chunk.getBiomeArray();
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