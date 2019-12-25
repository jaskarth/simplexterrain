package supercoder79.simplexterrain.configs.postprocessors;

import supercoder79.simplexterrain.SimplexTerrain;

public class ErosionConfigData {
	public String version = SimplexTerrain.VERSION;
	public int octaves = 4;
	public double frequency = 128.0;
	public double amplitudeHigh = 6.0;
	public double amplitudeLow = 8.0;

	public double baseNoise = 0.1;
	public double threshold = 0.0;
}
