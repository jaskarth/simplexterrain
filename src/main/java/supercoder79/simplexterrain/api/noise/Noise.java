package supercoder79.simplexterrain.api.noise;

/**
 * A class that can create 2d and 3d noise.
 * You *MUST* have at least one constructor in your implementation that takes in only a seed,
 * otherwise {@link OctaveNoiseSampler} won't work in conjunction with it.
 *
 * @author SuperCoder79
 */
public abstract class Noise {
	public Noise(long seed) {
	}
	public abstract double sample(double x, double z);
	public abstract double sample(double x, double y, double z);
}
