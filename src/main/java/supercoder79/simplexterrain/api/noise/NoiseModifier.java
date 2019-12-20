package supercoder79.simplexterrain.api.noise;

public interface NoiseModifier {

	void init(long seed);

	void setup();

	double modify(int x, int z, double currentNoiseValue, double scaleValue);
}
