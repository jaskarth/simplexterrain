package supercoder79.simplexterrain.configs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.annotations.SerializedName;

import supercoder79.simplexterrain.SimplexTerrain;
import supercoder79.simplexterrain.api.noise.NoiseType;
import supercoder79.simplexterrain.world.noisemodifier.NoiseModifiers;
import supercoder79.simplexterrain.world.postprocessor.PostProcessors;

public class MainConfigData {
	@SerializedName("configVersion") public String configVersion = SimplexTerrain.VERSION;
	@SerializedName("doModCompat") public boolean doModCompat = true;
	@SerializedName("addDetailNoise") public boolean addDetailNoise = true;

	@SerializedName("reloadConfigCommand") public boolean reloadConfigCommand = false;
	public boolean threadedNoiseGeneration = true;
	public boolean simplexIsDefault = false;
	public boolean deleteLakes = false;
	public boolean optimizeForRamUsage = false;
	public boolean generateVanillaCaves = true;

	public int noiseGenerationThreads = 2;

	public NoiseType noiseGenerator = NoiseType.SIMPLEX;

	public List<PostProcessors> postProcessors = new ArrayList<>();

	public List<NoiseModifiers> noiseModifiers = Arrays.asList(NoiseModifiers.MOUNTAINS, NoiseModifiers.RIDGES, NoiseModifiers.DETAILS);

	public int baseHeight = 100;

	public int baseOctaveAmount = 11;
	public int detailOctaveAmount = 2;
	public int scaleOctaveAmount = 2;
	public int peaksOctaveAmount = 2;

	public double baseNoiseFrequencyCoefficient = 0.75;
	public double baseNoiseSamplingFrequency = 1;

	public double detailAmplitudeHigh = 2;
	public double detailAmplitudeLow = 4;
	public double detailFrequency = 20;

	public double scaleAmplitudeHigh = 0.2;
	public double scaleAmplitudeLow = 0.09;
	public double scaleFrequencyExponent = 10;

	public double peaksFrequency = 750.0;
	public double peaksSampleOffset = -0.33;
	public double peaksAmplitude = 280.0;

	public double detailNoiseThreshold = 0.0;
	public double scaleNoiseThreshold  = -0.02;

	public int lowlandStartHeight = 66;
	public int midlandStartHeight = 90;
	public int highlandStartHeight = 140;
	public int toplandStartHeight = 190;

	public int biomeScaleAmount = 8;

	public double temperatureOffset = 0.0;
	public double humidityOffset = 0.0;

	public int seaLevel = 63;
	
	public int temperatureOctaveAmount = 1;
	public int humidityOctaveAmount = 2;
	public double temperatureFrequency = 15.0;
	public double humidityFrequency = 11.0;
	public double temperatureAmplitude = 1.2;
	public double humidityAmplitude = 1.2;
}
