package supercoder79.simplexterrain.configs.noisemodifiers;

import supercoder79.simplexterrain.SimplexTerrain;

public class MesaConfigData {
	public String version = SimplexTerrain.VERSION;
	public int terraceOctaves = 1;
	public int cutoffOctaves = 3;
	public double terraceFrequency = 100.0;
	public double cutoffFrequency = 1000.0;
	public double maximumScale = 8.0;
	public double noiseCutoff = 0.3;
	public double minTerraceThreshold = -0.3;
	public double minTerraceHeight = 14.0;
	public double maxTerraceThreshold = 0.3;
	public double maxTerraceHeight = 23;
	public double defaultTerraceHeight = 20;
}
