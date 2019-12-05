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

	public int implementedFunctions() {
		return NoiseImplementation.GOOD_ENOUGH;
	}

	//Helper noise functions =======================

	private static double fastSqrt(double d) {
		return Double.longBitsToDouble(((Double.doubleToLongBits(d)-(1l<<52))>>1 ) + (1l<<61));
	}

	private static int factorial(int n) {
		if (n == 1) {
			return 1;
		} else {
			return n * factorial(n-1);
		}
	}

	//ensures that the returned value is in [-1, 1]
	private double clamp(double value) {
		return (value > 1) ? 1 : (value < -1) ? -1 : value;
	}

	//ensures that the returned value is in [0, 1]
	private double clampPositive(double value) {
		return (value < 0) ? 0 : value;
	}

	private static double lerp(double progress, double start, double end) {
		return start + progress * (end - start);
	}

	private static double sigmoid(double x) {
		return (1/( 1 + Math.exp(-x)));
	}
}
