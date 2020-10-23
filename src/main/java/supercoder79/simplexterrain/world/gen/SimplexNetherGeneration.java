package supercoder79.simplexterrain.world.gen;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import supercoder79.simplexterrain.noise.gradient.SimplexStyleNoise;

public class SimplexNetherGeneration {

    private static SimplexStyleNoise mainNoise;
    private static SimplexStyleNoise detailsNoise;
    
    public static void init(long seed) {
        mainNoise = new SimplexStyleNoise(seed + 12);
        detailsNoise = new SimplexStyleNoise(seed + 20);
    }
    
    public static void generate(WorldAccess world, Chunk chunk, BiomeSource biomeSource, int seaLevel) {
        //TODO: threading

        BlockPos.Mutable posMutable = new BlockPos.Mutable();

        for (int x = 0; x < 16; x++) {
            posMutable.setX(x);

            for (int z = 0; z < 16; z++) {
                posMutable.setZ(z);

                double depth = 0;
                double scale = 0;

                for (int x1 = -1; x1 <= 1; x1++) {
                    for (int z1 = -1; z1 <= 1; z1++) {
                        Biome biome = biomeSource.getBiomeForNoiseGen((chunk.getPos().x*16) + (x + x1), 32, (chunk.getPos().z*16) + (z + z1));

                        depth += biome.getDepth();
                        scale += biome.getScale();
                    }
                }

                depth /= 9;
                scale /= 9;

                for (int y = 0; y < 127; y++) {
                    posMutable.setY(y);
                    if (getNoiseAt(depth, scale, (chunk.getPos().x*16) + x, y, (chunk.getPos().z*16) + z) > 0) {
                        chunk.setBlockState(posMutable, Blocks.NETHERRACK.getDefaultState(), false);
                    } else if (y < seaLevel) {
                        chunk.setBlockState(posMutable, Blocks.LAVA.getDefaultState(), false);
                    }
                }
            }
        }
    }

    private static double getNoiseAt(double depth, double scale, int x, int y, int z) {
        double noise = mainNoise.sample(x / 80.0, y / 60.0, z / 80.0) * (1 + depth);
        noise += detailsNoise.sample(x / 24.0, y / 18.0, z / 24.0) * 0.125;

        noise /= (1 - scale);

        noise += Math.max((22.0 / y) - 1, 0); // lower bound
        noise += Math.max((-22.0 / (y - 130)) - 1, 0); // upper bound

        return noise;
    }
}