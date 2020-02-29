package supercoder79.simplexterrain.world.postprocessor;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import supercoder79.simplexterrain.api.Heightmap;
import supercoder79.simplexterrain.api.postprocess.TerrainPostProcessor;
import supercoder79.simplexterrain.noise.gradient.OpenSimplexNoise;
import supercoder79.simplexterrain.noise.gradient.SimplexStyleNoise;

import java.util.Arrays;
import java.util.Random;

public class SimplexCavesFix implements TerrainPostProcessor {
    private SimplexStyleNoise sampler1;
    private SimplexStyleNoise sampler2;
    private SimplexStyleNoise threshold;
    private SimplexStyleNoise threshold2;
    private SimplexStyleNoise[] multiEvalInstances;

    @Override
    public void init(long seed) {
        sampler1 = new SimplexStyleNoise(seed + 79);
        sampler2 = new SimplexStyleNoise(seed - 79);
        threshold = new SimplexStyleNoise(seed + 89);
        threshold2 = new SimplexStyleNoise(seed - 89);
        multiEvalInstances = new SimplexStyleNoise[] { sampler1, sampler2, threshold };
    }

    @Override
    public void setup() {

    }

    @Override
    public void process(IWorld world, Random rand, int chunkX, int chunkZ, Heightmap heightmap) {
        int[] heights = heightmap.getHeightsInChunk(new ChunkPos(chunkX, chunkZ));

        double[] values = new double[multiEvalInstances.length];

        BlockPos.Mutable pos = new BlockPos.Mutable();
        for (int x = 0; x < 16; x++) {
            pos.setX(chunkX*16 + x);
            for (int z = 0; z < 16; z++) {
                pos.setZ(chunkZ*16 + z);
                int h = heights[x*16 + z]+1;
                for (int y = 1; y < h; y++) {

                    // Begin to compute threshold
                    double a = (h - y) - 5.0;
                    if (a < 0) a = 0; else a *= a;
                    a += 2.3;
                    double thresholdBase = (0.03125 / (y + 2.0)) - (0.0234375 / a) + 0.0125;
                    double thresholdAmp1 = 0.06125 / (y + 7.0);
                    double thresholdSubAmp2 = 0.04625;

                    // Reset values for Y, and calculate noises for this block
                    Arrays.fill(values, 0);
                    SimplexStyleNoise.noise3_XZBeforeY(multiEvalInstances, pos.getX() / 70f, y / 50f, pos.getZ() / 70f, values);
                    double d1 = values[0], d2 = values[1], thresholdNoise1 = values[2];

                    // Sum of squared noise values, near zero produces tunnels
                    double d = (d1 * d1) + (d2 * d2);

                    // If the sum of squares is above the maximum value the threshold can take given what we know,
                    // then we know we don't remove the block here.
                    double largestPossibleThreshold = thresholdBase + thresholdNoise1 * (thresholdAmp1 + (thresholdNoise1 > 0 ? thresholdSubAmp2 :  -thresholdSubAmp2));
                    if (d > largestPossibleThreshold) continue;

                    // If the sum of squares is below the minimum value the threshold can take given what we know,
                    // then we know we do remove the block here.
                    double smallestPossibleThreshold = thresholdBase + thresholdNoise1 * (thresholdAmp1 + (thresholdNoise1 > 0 ? -thresholdSubAmp2 : thresholdSubAmp2));
                    boolean removeBlock = (d < smallestPossibleThreshold);

                    // If it's in the range where the second threshold noise makes a difference,
                    // compute that and then we have our final answer.
                    if (!removeBlock) {
                        double thresholdNoise2 = threshold2.sample(pos.getX() / 15f, y / 10f, pos.getZ() / 15f);
                        double threshold = thresholdBase + thresholdNoise1 * (thresholdAmp1 + thresholdNoise2 * thresholdSubAmp2);
                        removeBlock = (d < threshold);
                    }

                    if (removeBlock) {
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
}
