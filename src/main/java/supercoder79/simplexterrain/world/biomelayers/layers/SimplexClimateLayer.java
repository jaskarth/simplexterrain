package supercoder79.simplexterrain.world.biomelayers.layers;

import net.minecraft.world.biome.layer.type.InitLayer;
import net.minecraft.world.biome.layer.util.LayerRandomnessSource;
import supercoder79.simplexterrain.api.SimplexClimate;
import supercoder79.simplexterrain.noise.OpenSimplexNoise;

public class SimplexClimateLayer implements InitLayer {
	public SimplexClimateLayer(long worldSeed) {
		temperatureNoise = new OpenSimplexNoise(worldSeed - 5);
		humidityNoise = new OpenSimplexNoise(worldSeed + 5);
	}
	
	private final OpenSimplexNoise temperatureNoise;
	private final OpenSimplexNoise humidityNoise;
	
	@Override
	public int sample(LayerRandomnessSource rand, int x, int z) {
		double temperature = temperatureNoise.sample(transformTemperatureXZ(x), transformTemperatureXZ(z));
		double humidity = humidityNoise.sample(transformHumidityXZ(x), transformHumidityXZ(z));
		
		return SimplexClimate.fromTemperatureHumidity(temperature, humidity).id;
	}
	
	private double transformTemperatureXZ(double value) {
		return value / 9D;
	}
	
	private double transformHumidityXZ(double value) {
		return value / 6D;
	}
	
	public static final SimplexClimate[] REVERSE_ID_MAP = new SimplexClimate[9];
}
