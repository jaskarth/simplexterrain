package supercoder79.simplexterrain.api.biomes;

import net.minecraft.world.biome.Biome;
import supercoder79.simplexterrain.world.gen.SimplexNetherGeneration;

/**
 * API front for Simplex Terrain's Nether generator.
 *
 * @author SuperCoder79
 */
public final class SimplexNether {
    private SimplexNether() {

    }

    /**
     * Set's a biome's expansiveness instead of using the default 1.0.
     * Expansivness dictates the chaos and size of the biome's air cavities.
     * High values constrict the biome while low values expand them.
     * Negative numbers are supported.
     *
     * @param biome The biome to add.
     * @param value The biome's expansiveness.
     *
     * @author SuperCoder79.
     */
    public static void setBiomeExpansiveness(Biome biome, double value) {
        SimplexNetherGeneration.biomeToExpansivenessMap.put(biome, value);
    }
}
