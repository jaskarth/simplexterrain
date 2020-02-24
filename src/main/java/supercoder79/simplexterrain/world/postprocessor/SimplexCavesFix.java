package supercoder79.simplexterrain.world.postprocessor;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkRandom;
import supercoder79.simplexterrain.api.Heightmap;
import supercoder79.simplexterrain.api.cache.AbstractSampler;
import supercoder79.simplexterrain.api.cache.CacheSampler;
import supercoder79.simplexterrain.api.noise.OctaveNoiseSampler;
import supercoder79.simplexterrain.api.postprocess.TerrainPostProcessor;
import supercoder79.simplexterrain.noise.gradient.OpenSimplexNoise;

import java.util.Random;

public class SimplexCavesFix implements TerrainPostProcessor {
    private OpenSimplexNoise sampler1;
    private OpenSimplexNoise sampler2;
    private OpenSimplexNoise threshold;
    private OpenSimplexNoise threshold2;

    @Override
    public void init(long seed) {
        sampler1 = new OpenSimplexNoise(seed + 79);
        sampler2 = new OpenSimplexNoise(seed - 79);
        threshold = new OpenSimplexNoise(seed + 89);
        threshold2 = new OpenSimplexNoise(seed - 89);
    }

    @Override
    public void setup() {

    }

    @Override
    public void process(IWorld world, Random rand, int chunkX, int chunkZ, Heightmap heightmap) {
        int[] heights = heightmap.getHeightsInChunk(new ChunkPos(chunkX, chunkZ));

        BlockPos.Mutable pos = new BlockPos.Mutable();
        for (int x = 0; x < 16; x++) {
            pos.setX(chunkX*16 + x);
            for (int z = 0; z < 16; z++) {
                pos.setZ(chunkZ*16 + z);
                int h = heights[x*16 + z]+1;
                for (int y = 1; y < h; y++) {
                    double d1 = sampler1.sample(pos.getX() / 20f, y / 10f, pos.getZ() / 20f);
                    double d2 = sampler2.sample(pos.getX() / 20f, y / 10f, pos.getZ() / 20f);
                    double d = (d1 * d1) + (d2 * d2);
                    if (d > getThreshold(pos.getX(), y, pos.getZ(), h)) {
                        pos.setY(y);
                        if (world.getBlockState(pos) == Blocks.BEDROCK.getDefaultState()) continue;

                        if (y < 11) {
                            world.setBlockState(pos, Blocks.LAVA.getDefaultState(), 0);
                        } else {
                            world.setBlockState(pos, Blocks.AIR.getDefaultState(), 0);
                        }
                    }
                }
            }
        }
    }

    private double getThreshold(int x, int y, int z, int height) {
        double c = (1 / (float)(y + 2)) + (3 / (float)(Math.abs(height - y)) + 0.4);
        return c + (threshold.sample(x / 20f, y / 10f, z / 20f) * ((1 / (float)(y + 7) + (threshold2.sample(x / 4f, y / 2f, z / 4f) * 0.2))));
    }
}
