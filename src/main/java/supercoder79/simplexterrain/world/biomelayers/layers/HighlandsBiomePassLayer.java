package supercoder79.simplexterrain.world.biomelayers.layers;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.layer.util.LayerRandomnessSource;
import supercoder79.simplexterrain.world.biome.BiomePicker;
import supercoder79.simplexterrain.world.biome.SimplexBiomesImpl;

public enum HighlandsBiomePassLayer implements BiomePassLayer {
    INSTANCE;

	@Override
    public int sample(LayerRandomnessSource rand, int value) {
        BiomePicker picker = SimplexBiomesImpl.getHighlandsBiomePicker(SimplexClimateLayer.REVERSE_ID_MAP[value]);
        
        return picker == null ? TAIGA : Registry.BIOME.getRawId(picker.pickBiome(rand));
    }
}