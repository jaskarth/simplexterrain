package supercoder79.simplexterrain.terrain.biomelayers.layers;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.layer.type.SouthEastSamplingLayer;
import net.minecraft.world.biome.layer.util.LayerRandomnessSource;
import supercoder79.simplexterrain.api.LandBiomeLayerHolder;

import java.util.Map;

public enum HighlandsBiomePassLayer implements SouthEastSamplingLayer {
    INSTANCE;

    @Override
    public int sample(LayerRandomnessSource layerRandomnessSource, int se) {
        if (se == Registry.BIOME.getRawId(Biomes.TAIGA)) {
            for (Map.Entry<Integer, Integer> entry : LandBiomeLayerHolder.highlandBiomes.entrySet()) {
                if (layerRandomnessSource.nextInt(entry.getValue()) == 0) return entry.getKey();
            }
        }
        return se;
    }
}