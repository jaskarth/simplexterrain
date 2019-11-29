package supercoder79.simplexterrain.terrain.biomelayers.layers;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.layer.type.SouthEastSamplingLayer;
import net.minecraft.world.biome.layer.util.LayerRandomnessSource;

public enum LowlandsBiomePassLayer implements SouthEastSamplingLayer {
    INSTANCE;

    @Override
    public int sample(LayerRandomnessSource layerRandomnessSource, int se) {
        if (se == Registry.BIOME.getRawId(Biomes.PLAINS)) {
            if (layerRandomnessSource.nextInt(8) == 0) return Registry.BIOME.getRawId(Biomes.SWAMP);
            if (layerRandomnessSource.nextInt(8) == 0) return Registry.BIOME.getRawId(Biomes.DESERT);
            if (layerRandomnessSource.nextInt(8) == 0) return Registry.BIOME.getRawId(Biomes.SAVANNA);
            if (layerRandomnessSource.nextInt(8) == 0) return Registry.BIOME.getRawId(Biomes.JUNGLE);
        }
        return se;
    }
}
