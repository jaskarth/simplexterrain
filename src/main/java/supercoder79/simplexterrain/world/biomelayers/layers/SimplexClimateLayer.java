package supercoder79.simplexterrain.world.biomelayers.layers;

import net.minecraft.world.biome.layer.type.InitLayer;
import net.minecraft.world.biome.layer.util.LayerRandomnessSource;
import supercoder79.simplexterrain.api.Heightmap;
import supercoder79.simplexterrain.api.SimplexClimate;
import supercoder79.simplexterrain.noise.OctaveOpenSimplexNoise;
import supercoder79.simplexterrain.noise.OpenSimplexNoise;

import java.util.Random;

public class SimplexClimateLayer implements InitLayer {
	public SimplexClimateLayer(long worldSeed) {
		temperatureNoise = new OctaveOpenSimplexNoise(new Random(worldSeed - 5), 4, 8, 1, 1);
		humidityNoise = new OctaveOpenSimplexNoise(new Random(worldSeed + 5), 4, 8, 1, 1);
	}
	
	private final OctaveOpenSimplexNoise temperatureNoise;
	private final OctaveOpenSimplexNoise humidityNoise;
	
	@Override
	public int sample(LayerRandomnessSource rand, int x, int z) {
		double temperature = temperatureNoise.sample(transformTemperatureXZ(x), transformTemperatureXZ(z));
		double humidity = humidityNoise.sample(transformHumidityXZ(x), transformHumidityXZ(z));
		System.out.println(x + " : " + z);
		
		return SimplexClimate.fromTemperatureHumidity(temperature, humidity).id;
	}
	
	private double transformTemperatureXZ(double value) {
		return value / 9D;
	}
	
	private double transformHumidityXZ(double value) {
		return value / 6D;
	}
	
	public static final SimplexClimate[] REVERSE_ID_MAP = new SimplexClimate[10];
}
