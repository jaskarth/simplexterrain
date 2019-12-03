package supercoder79.simplexterrain.world.biomelayers.layers;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.layer.util.LayerRandomnessSource;
import supercoder79.simplexterrain.impl.BiomePicker;
import supercoder79.simplexterrain.impl.SimplexBiomesImpl;

public enum MountainPeaksBiomePassLayer implements BiomePassLayer {
    INSTANCE;

	@Override
    public int sample(LayerRandomnessSource rand, int value) {
        BiomePicker picker = SimplexBiomesImpl.getMountainPeaksBiomePicker(SimplexClimateLayer.REVERSE_ID_MAP[value]);
        
        return picker == null ? MOUNTAINS : Registry.BIOME.getRawId(picker.pickBiome(rand));
    }
}