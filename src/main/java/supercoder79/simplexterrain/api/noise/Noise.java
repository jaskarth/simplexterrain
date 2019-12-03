package supercoder79.simplexterrain.api.noise;

public abstract class Noise {
	public Noise(long seed) {
	}
	public abstract double sample(double x, double z);
	public abstract double sample(double x, double y, double z);
}
