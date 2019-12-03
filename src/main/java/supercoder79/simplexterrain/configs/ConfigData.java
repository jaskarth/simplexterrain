package supercoder79.simplexterrain.configs;

public class ConfigData {
    public boolean doModCompat = true;
    public boolean addDetailNoise = true;

    public int baseHeight = 100;

    public int baseOctaveAmount = 11;
    public int detailOctaveAmount = 2;
    public int scaleOctaveAmount = 2;

    public double baseNoiseFrequencyCoefficient = 0.75;
    public double baseNoiseSamplingFrequency = 1;

    public double detailAmplitudeHigh = 2;
    public double detailAmplitudeLow = 4;
    public double detailFrequency = 20;

    public double scaleAmplitudeHigh = 0.2;
    public double scaleAmplitudeLow = 0.09;
    public double scaleFrequencyExponent = 10;

    public double detailNoiseThreshold = 0.0;
    public double scaleNoiseThreshold  = -0.02;

    public int lowlandStartHeight = 68;
    public int midlandStartHeight = 90;
    public int highlandStartHeight = 140;
    public int toplandStartHeight = 190;

    public int lowlandBiomeAdditionAttempts = 2;
    public int midlandBiomeAdditionAttempts = 2;
    public int highlandBiomeAdditionAttempts = 2;
    public int toplandBiomeAdditionAttempts = 2;

    public int lowlandBiomeScaleAmount = 7;
    public int midlandBiomeScaleAmount = 7;
    public int highlandBiomeScaleAmount = 7;
    public int toplandBiomeScaleAmount = 7;
}
