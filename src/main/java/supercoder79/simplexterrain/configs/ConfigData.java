package supercoder79.simplexterrain.configs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import supercoder79.simplexterrain.SimplexTerrain;
import supercoder79.simplexterrain.api.noise.NoiseType;
import supercoder79.simplexterrain.world.noisemodifier.NoiseModifiers;
import supercoder79.simplexterrain.world.postprocessor.PostProcessors;

public class ConfigData {
	public String configVersion = SimplexTerrain.VERSION;
	public boolean doModCompat = true;
	public boolean addDetailNoise = true;
	public boolean addPeaksNoise = true;

	public boolean reloadConfigCommand = false;
	public boolean optimizeChunkGenerationInvasively = true;
	public boolean threadedNoiseGeneration = true;

	public NoiseType noiseGenerator = NoiseType.SIMPLEX;

	public List<PostProcessors> postProcessors = new ArrayList<>(); //TODO: replace default post processors

	public List<NoiseModifiers> noiseModifiers = Arrays.asList(NoiseModifiers.PEAKS, NoiseModifiers.MESA);

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

	public int lowlandStartHeight = 68;
	public int midlandStartHeight = 90;
	public int highlandStartHeight = 140;
	public int toplandStartHeight = 190;

	public int biomeScaleAmount = 7;

	public double temperatureOffset = 0.0;
	public double humidityOffset = 0.0;

	public int seaLevel = 63;
}
