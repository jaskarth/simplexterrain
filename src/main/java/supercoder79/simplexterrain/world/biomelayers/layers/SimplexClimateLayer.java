package supercoder79.simplexterrain.world.biomelayers.layers;

import java.util.Random;

import net.minecraft.world.biome.layer.type.InitLayer;
import net.minecraft.world.biome.layer.util.LayerRandomnessSource;
import supercoder79.simplexterrain.SimplexTerrain;
import supercoder79.simplexterrain.api.biomes.SimplexClimate;
import supercoder79.simplexterrain.api.noise.OctaveNoiseSampler;
import supercoder79.simplexterrain.noise.gradient.OpenSimplexNoise;

public class SimplexClimateLayer implements InitLayer {
	private OctaveNoiseSampler temperatureNoise;
	private OctaveNoiseSampler humidityNoise;

	private final long worldSeed;

	private static final Random RAND = new Random();

	public SimplexClimateLayer(long worldSeed) {
		this.worldSeed = worldSeed;
		this.initialiseNoise();
	}
	
	public void initialiseNoise() {
		RAND.setSeed(worldSeed);
		temperatureNoise = new OctaveNoiseSampler<>(OpenSimplexNoise.class, RAND, SimplexTerrain.CONFIG.temperatureOctaveAmount, SimplexTerrain.CONFIG.temperatureFrequency, SimplexTerrain.CONFIG.temperatureAmplitude, SimplexTerrain.CONFIG.temperatureAmplitude);
		humidityNoise = new OctaveNoiseSampler<>(OpenSimplexNoise.class, RAND, SimplexTerrain.CONFIG.humidityOctaveAmount, SimplexTerrain.CONFIG.humidityFrequency, SimplexTerrain.CONFIG.humidityAmplitude, SimplexTerrain.CONFIG.humidityAmplitude);
	}

	@Override
	public int sample(LayerRandomnessSource rand, int x, int z) {
		double temperature = temperatureNoise.sample(x, z) + SimplexTerrain.CONFIG.temperatureOffset;
		double humidity = humidityNoise.sample(x, z) + SimplexTerrain.CONFIG.humidityOffset;

		return SimplexClimate.fromTemperatureHumidity(temperature, humidity).id;
	}

	public static final SimplexClimate[] REVERSE_ID_MAP = new SimplexClimate[10];
}
