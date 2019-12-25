package supercoder79.simplexterrain.world.biomelayers.layers;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.layer.type.IdentitySamplingLayer;
import net.minecraft.world.biome.layer.util.LayerRandomnessSource;
import supercoder79.simplexterrain.api.biomes.SimplexClimate;

import java.util.function.ToIntFunction;

public enum ClimateTransformerLayer implements IdentitySamplingLayer {
	SHORES(climate -> Registry.BIOME.getRawId(climate.oceanSet.shore)),
	OCEAN(climate -> Registry.BIOME.getRawId(climate.oceanSet.ocean)),
	DEEP_OCEAN(climate -> Registry.BIOME.getRawId(climate.oceanSet.deepOcean));

	private ToIntFunction<SimplexClimate> transformer;

	ClimateTransformerLayer(ToIntFunction<SimplexClimate> transformer) {
		this.transformer = transformer;
	}

	@Override
	public int sample(LayerRandomnessSource rand, int value) {
		return this.transformer.applyAsInt(SimplexClimateLayer.REVERSE_ID_MAP[value]);
	}
}
