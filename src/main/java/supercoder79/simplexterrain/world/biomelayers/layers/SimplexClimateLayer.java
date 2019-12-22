package supercoder79.simplexterrain.world.biomelayers.layers;

import net.minecraft.world.biome.layer.type.InitLayer;
import net.minecraft.world.biome.layer.util.LayerRandomnessSource;
import supercoder79.simplexterrain.SimplexTerrain;
import supercoder79.simplexterrain.api.biomes.SimplexClimate;
import supercoder79.simplexterrain.api.noise.OctaveNoiseSampler;
import supercoder79.simplexterrain.noise.gradient.OpenSimplexNoise;

import java.util.Random;

public class SimplexClimateLayer implements InitLayer {
	private final OctaveNoiseSampler temperatureNoise;
	private final OctaveNoiseSampler humidityNoise;

	public SimplexClimateLayer(long worldSeed) {
		Random rand = new Random(worldSeed);
		temperatureNoise = new OctaveNoiseSampler<>(OpenSimplexNoise.class, rand, 1, 7, 1.5, 1.5);
		humidityNoise = new OctaveNoiseSampler<>(OpenSimplexNoise.class, rand, 2, 5, 1.5, 1.5);
	}

	@Override
	public int sample(LayerRandomnessSource rand, int x, int z) {
		double temperature = temperatureNoise.sample(x, z) + SimplexTerrain.CONFIG.temperatureOffset;
		double humidity = humidityNoise.sample(x, z) + SimplexTerrain.CONFIG.humidityOffset;

		return SimplexClimate.fromTemperatureHumidity(temperature, humidity).id;
	}

	public static final SimplexClimate[] REVERSE_ID_MAP = new SimplexClimate[10];
}
