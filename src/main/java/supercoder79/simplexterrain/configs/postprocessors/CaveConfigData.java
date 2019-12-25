package supercoder79.simplexterrain.configs.postprocessors;

public class CaveConfigData {
	public int caveNoiseOctaves = 6;
	public int caveHeightOctaves = 5;
	public int caveEnabledOctaves = 2;

	public double caveNoiseFrequency = 2048.0;
	public double caveHeightFrequency = 32.0;
	public double caveEnabledFrequency = 1024.0;

	public double caveNoiseAmplitudeHigh = 14.0;
	public double caveNoiseAmplitudeLow = 14.0;

	public double caveHeightAmplitudeHigh = 10.0;
	public double caveHeightAmplitudeLow = 10.0;

	public double caveEnabledAmplitudeHigh = 1.0;
	public double caveEnabledAmplitudeLow = 1.0;

	public double caveEnabledThreshold = 0.0;
	public int baseHeight = 30;

	public int caveDeletionThreshold = 3;

}
