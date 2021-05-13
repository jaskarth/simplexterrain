package supercoder79.simplexterrain.world.biomelayers.layers;

import java.util.Map;
import java.util.Set;

import net.minecraft.util.Pair;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.layer.type.IdentitySamplingLayer;
import net.minecraft.world.biome.layer.util.LayerRandomnessSource;
import supercoder79.simplexterrain.world.biome.SimplexBiomesImpl;

public enum ReplaceBiomesLayer implements IdentitySamplingLayer {
    INSTANCE;

    @Override
    public int sample(LayerRandomnessSource layerRandomnessSource, int i) {
//        Set<Map.Entry<Biome, Pair<Biome, Integer>>> entries = SimplexBiomesImpl.getReplacementBiomes().entrySet();
//
//        for (Map.Entry<Biome, Pair<Biome, Integer>> entry : entries) {
//            if (i == Registry.BIOME.getRawId(entry.getKey())) {
//                if (layerRandomnessSource.nextInt(entry.getValue().getRight()) == 0) {
//                    return Registry.BIOME.getRawId(entry.getKey());
//                }
//            }
//        }
        return i;
    }
}
