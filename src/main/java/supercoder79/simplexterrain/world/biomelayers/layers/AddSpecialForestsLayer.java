package supercoder79.simplexterrain.world.biomelayers.layers;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.layer.type.IdentitySamplingLayer;
import net.minecraft.world.biome.layer.util.LayerRandomnessSource;

public enum AddSpecialForestsLayer implements IdentitySamplingLayer {
    INSTANCE;

    @Override
    public int sample(LayerRandomnessSource layerRandomnessSource, int i) {
        if (i == Registry.BIOME.getRawId(Biomes.BIRCH_FOREST)) {
            if (layerRandomnessSource.nextInt(6) == 0) return Registry.BIOME.getRawId(Biomes.TALL_BIRCH_FOREST);
        }
        if (i == Registry.BIOME.getRawId(Biomes.FOREST)) {
            if (layerRandomnessSource.nextInt(6) == 0) return Registry.BIOME.getRawId(Biomes.FLOWER_FOREST);
        }
        return i;
    }
}
