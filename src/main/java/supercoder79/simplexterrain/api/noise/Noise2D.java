package supercoder79.simplexterrain.api.noise;

/**
 * An interface specifying a 2d noise function.
 */
@FunctionalInterface
public interface Noise2D {
	double sample(double x, double z);
}
