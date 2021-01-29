package supercoder79.simplexterrain.world.biomelayers.layers;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.layer.type.IdentitySamplingLayer;
import net.minecraft.world.biome.layer.util.LayerRandomnessSource;

public class BaseBiomesLayer implements IdentitySamplingLayer {
    private final Registry<Biome> registry;

    public BaseBiomesLayer(Registry<Biome> registry) {

        this.registry = registry;
    }

    @Override
    public int sample(LayerRandomnessSource context, int value) {
        return this.registry.getRawId(context.nextInt(3) == 0 ? this.registry.get(BiomeKeys.FOREST) : this.registry.get(BiomeKeys.PLAINS));
    }
}
