package supercoder79.simplexterrain.terrain;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.SystemUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.noise.OctavePerlinNoiseSampler;
import net.minecraft.util.math.noise.OctaveSimplexNoiseSampler;
import net.minecraft.util.math.noise.PerlinNoiseSampler;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.layer.BiomeLayers;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.ProtoChunk;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.chunk.OverworldChunkGeneratorConfig;
import net.minecraft.world.gen.chunk.SurfaceChunkGenerator;
import supercoder79.simplexterrain.api.Heightmap;

public class WorldChunkGenerator extends SurfaceChunkGenerator<OverworldChunkGeneratorConfig> implements Heightmap {
    private static final float[] BIOME_WEIGHT_TABLE = SystemUtil.consume(new float[25], (fs) -> {
        for(int i = -2; i <= 2; ++i) {
            for(int j = -2; j <= 2; ++j) {
                float f = 10.0F / MathHelper.sqrt((float)(i * i + j * j) + 0.2F);
                fs[i + 2 + (j + 2) * 5] = f;
            }
        }
    });

    double[] heightNoise;
    OctavePerlinNoiseSampler noiseSampler = new OctavePerlinNoiseSampler(this.random, 16);

    public static OctaveSimplexNoiseSampler[] NOISE_SAMPLERS;


    public WorldChunkGenerator(IWorld iWorld_1, BiomeSource biomeSource_1, OverworldChunkGeneratorConfig chunkGeneratorConfig_1) {
        super(iWorld_1, biomeSource_1, 4, 8, 256, chunkGeneratorConfig_1, false);
        NOISE_SAMPLERS = new OctaveSimplexNoiseSampler[64];
        for(int i = 1; i <= 64; i++) NOISE_SAMPLERS[i-1] = new OctaveSimplexNoiseSampler(this.random, i);
        ((WorldBiomeSource)(this.biomeSource)).setHeightmap(this);
    }

    @Override
    protected double[] computeNoiseRange(int x, int z) {
        double[] ds = new double[2];
        float f = 0.0F;
        float g = 0.0F;
        float h = 0.0F;
        float j = this.biomeSource.getBiomeForNoiseGen(x, z).getDepth();

        for(int k = -2; k <= 2; ++k) {
            for(int l = -2; l <= 2; ++l) {
                Biome biome = this.biomeSource.getBiomeForNoiseGen(x + k, z + l);
                float m = biome.getDepth();
                float n = biome.getScale();

                float o = BIOME_WEIGHT_TABLE[k + 2 + (l + 2) * 5] / (m + 3.0F);
                if (biome.getDepth() > j) {
                    o /= 2.0F;
                }

                f += n * o;
                g += m * o;
                h += o;
            }
        }

        f /= h;
        g /= h;
        f = f * 0.9F + 0.1F;
        g = (g * 4.0F - 1.0F) / 8.0F;
        ds[0] = (double)g + this.sampleNoise(x, z);
        ds[1] = (double)f;
        return ds;
    }

    private double sampleNoise(int x, int y) {
        double d = noiseSampler.sample((double)(x * 200), 10.0D, (double)(y * 200), 1.0D, 0.0D, true) / 4.0D;
        if (d < 0.0D) {
            d = -d * 0.3D;
        }

        d = d * 7.0D - 2.0D;
        if (d < 0.0D) {
            d /= 28.0D;
        } else {
            if (d > 1.0D) {
                d = 1.0D;
            }

            d /= 8.0D;
        }

        return d;
    }

    @Override
    protected double computeNoiseFalloff(double depth, double scale, int y) {
//        return Math.pow(depth*16*16, scale)*y;
        double e = ((double)y - (8.5D + depth * 8.5D / 8.0D * 4.0D)) * 12.0D * 128.0D / 256.0D / scale;
        if (e < 0.0D) {
            e *= 4.0D;
        }

        return e;
    }

    @Override
    protected void sampleNoiseColumn(double[] buffer, int x, int z) {
//        this.sampleNoiseColumn(buffer, x, z, 684.4119873046875D, 684.4119873046875D, 8.555149841308594D, 4.277574920654297D, 3, -10);
        this.sampleNoiseColumn(buffer, x, z, 2048, 256, 28, 16, 64, -3000);
    }

    @Override
    public int getSpawnHeight() {
        return 0;
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
        return  (int) (NOISE_SAMPLERS[10].sample(x, z, true)*0.1 + 100);
    }
}