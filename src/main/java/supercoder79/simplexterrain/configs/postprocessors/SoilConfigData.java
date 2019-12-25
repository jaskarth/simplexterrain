package supercoder79.simplexterrain.configs.postprocessors;

public class SoilConfigData {
	public int coarseDirtOctaves = 4;
	public double coarseDirtFrequency = 256.0;
	public double coarseDirtAmplitudeHigh = 6.0;
	public double coarseDirtAmplitudeLow = 8.0;

	public int podzolOctaves = 4;
	public double podzolFrequency = 256.0;
	public double podzolAmplitudeHigh = 6.0;
	public double podzolAmplitudeLow = 8.0;

	public double coarseDirtThreshold = 0.65;
	public double podzolThreshold = -0.65;
}
