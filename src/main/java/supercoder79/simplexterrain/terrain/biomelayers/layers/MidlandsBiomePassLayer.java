package supercoder79.simplexterrain.terrain.biomelayers.layers;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.layer.type.SouthEastSamplingLayer;
import net.minecraft.world.biome.layer.util.LayerRandomnessSource;

public enum MidlandsBiomePassLayer implements SouthEastSamplingLayer {
    INSTANCE;

    @Override
    public int sample(LayerRandomnessSource layerRandomnessSource, int se) {
        if (se == Registry.BIOME.getRawId(Biomes.FOREST)) {
            if (layerRandomnessSource.nextInt(6) == 0) return Registry.BIOME.getRawId(Biomes.BIRCH_FOREST);
            if (layerRandomnessSource.nextInt(6) == 0) return Registry.BIOME.getRawId(Biomes.DARK_FOREST);
        }
        return se;
    }
}
