package supercoder79.simplexterrain.api.noisemodifier;

public interface NoiseModifier {
	void init(long seed);
	void setup();
	double modify(int x, int z, double currentNoiseValue);
	String getName();
}
