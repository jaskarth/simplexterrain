package supercoder79.simplexterrain.world.biomelayers.layers;

import java.util.function.ToIntFunction;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.layer.type.IdentitySamplingLayer;
import net.minecraft.world.biome.layer.util.LayerRandomnessSource;
import supercoder79.simplexterrain.api.biomes.SimplexClimate;

public class ClimateTransformerLayer implements IdentitySamplingLayer {

	public static ClimateTransformerLayer shore(Registry<Biome> biomes) {
		return new ClimateTransformerLayer(climate -> biomes.getRawId(biomes.get(climate.oceanSet.shore)));
	}

	public static ClimateTransformerLayer ocean(Registry<Biome> biomes) {
		return new ClimateTransformerLayer(climate -> biomes.getRawId(biomes.get(climate.oceanSet.ocean)));
	}

	public static ClimateTransformerLayer deepOcean(Registry<Biome> biomes) {
		return new ClimateTransformerLayer(climate -> biomes.getRawId(biomes.get(climate.oceanSet.deepOcean)));
	}

	private final ToIntFunction<SimplexClimate> transformer;

	ClimateTransformerLayer(ToIntFunction<SimplexClimate> transformer) {
		this.transformer = transformer;
	}

	@Override
	public int sample(LayerRandomnessSource rand, int value) {
		return this.transformer.applyAsInt(SimplexClimateLayer.REVERSE_ID_MAP[value]);
	}
}
