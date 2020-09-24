package supercoder79.simplexterrain.world.gen;

import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import supercoder79.simplexterrain.SimplexTerrain;
import supercoder79.simplexterrain.noise.gradient.SimplexStyleNoise;

import java.util.HashMap;
import java.util.Map;

public class SimplexNetherGeneration {
    public static Map<Identifier, Double> biomeToExpansivenessMap = new HashMap<>();

    private static SimplexStyleNoise noise;
    private static SimplexStyleNoise noise2;
    private static SimplexStyleNoise lowerResolution;
    private static SimplexStyleNoise higherResolution;
    private static SimplexStyleNoise vertical;
    private static SimplexStyleNoise threshold;
    
    public static void init(long seed) {
        noise = new SimplexStyleNoise(seed + 12);
        noise2 = new SimplexStyleNoise(seed + 20);
        lowerResolution = new SimplexStyleNoise(seed + 21);
        higherResolution = new SimplexStyleNoise(seed - 21);
        vertical = new SimplexStyleNoise(seed + 22);
        threshold = new SimplexStyleNoise(seed - 20);
    }
    
    public static void generate(WorldAccess world, Chunk chunk, BiomeSource biomeSource, int seaLevel) {
        Registry<Biome> biomes = ((ChunkRegion)world).toServerWorld().getRegistryManager().get(Registry.BIOME_KEY);

        //TODO: threading

        BlockPos.Mutable posMutable = new BlockPos.Mutable();

        for (int x = 0; x < 16; x++) {
            posMutable.setX(x);

            for (int z = 0; z < 16; z++) {
                posMutable.setZ(z);

                double expansiveness = 0;

                for (int x1 = -1; x1 <= 1; x1++) {
                    for (int z1 = -1; z1 <= 1; z1++) {
                        expansiveness += biomeToExpansivenessMap.getOrDefault(
                                biomes.getId(biomeSource.getBiomeForNoiseGen((chunk.getPos().x*16) + (x + x1), 32, (chunk.getPos().z*16) + (z + z1))), 1.0);
                    }
                }

                expansiveness /= 9;

                for (int y = 0; y < 127; y++) {
                    posMutable.setY(y);
                    if (getNoiseAt(expansiveness, (chunk.getPos().x*16) + x, y, (chunk.getPos().z*16) + z) > getThreshold((chunk.getPos().x*16) + x, y, (chunk.getPos().z*16) + z)) {
                        chunk.setBlockState(posMutable, Blocks.NETHERRACK.getDefaultState(), false);
                    } else if (y < seaLevel) {
                        chunk.setBlockState(posMutable, Blocks.LAVA.getDefaultState(), false);
                    }
                }
            }
        }
    }

    private static double getNoiseAt(double expansiveness, int x, int y, int z) {

        double scale = SimplexTerrain.CONFIG.mainNetherScale;
        double scaleLow = scale / 2;
        double scaleHigh = scale * 2;

        double baseline = noise.sample(x / scale, y / scaleLow, z / scale);
        double addition = noise2.sample(x / scale, y / scaleLow, z / scale);
        double addition2 = lowerResolution.sample(x / scaleLow, y / scale / 1.5, z / scaleLow);
        double addition3 = higherResolution.sample(x / scaleHigh, y / scale, z / scaleHigh);
        double verticalNoise = vertical.sample(x / scale, y / scale / 10, z / scale);
        baseline += (15 / (float)y); //lower bound
        baseline += (-15 / ((float)(y - 130))); //upper bound
        return (baseline*0.55) + (addition*0.3*expansiveness) + (addition2*0.25*expansiveness) + (addition3*0.175*expansiveness) + (verticalNoise*0.1*expansiveness);
    }

    private static double getThreshold(int x, int y, int z) {
        double scale = SimplexTerrain.CONFIG.netherThresholdScale;

        return SimplexTerrain.CONFIG.netherThresholdBase + (threshold.sample(x / scale, y / scale / 2, z / scale) * SimplexTerrain.CONFIG.netherThresholdAmplitude);
    }
}