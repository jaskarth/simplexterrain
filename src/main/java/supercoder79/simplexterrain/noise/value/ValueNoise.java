package supercoder79.simplexterrain.noise.value;

import supercoder79.simplexterrain.api.noise.Noise;
import supercoder79.simplexterrain.api.noise.NoiseImplementation;

/**
 * @author Valoeghese
 */
public class ValueNoise extends Noise {

	public ValueNoise(long seed) {
		super(seed);
	}

	public static double[] q = {
			5, 16, 17, 24, 27, 0, 31, 12, 26,
			6, 13, 20, 19, 22, 1, 25, 29, 18,
			28, 14, 32, 7, 2, 10, 15, 23, 8,
			9, 4, 21, 3, 31, 11, 30};

	@Override
	public double sample(double x, double y, double z) {
		return 0;
	}

	@Override
	public double sample(double x, double z) {
		return (sampleImpl(x, z) * 2) - 1;
	}

	public double sampleImpl(double x, double z) {
		int x0 = floor(x);
		int z0 = floor(z);
		int x1 = ceil(x);
		int z1 = ceil(z);
		x -= x0;
		z -= z0;
		x = fade(x);
		z = fade(z);

		double nw = qVal(x0, z0);
		double ne = qVal(x1, z0);
		double sw = qVal(x0, z1);
		double se = qVal(x1, z1);

		return lerp(
				z,
				lerp(x, nw, ne),
				lerp(z, sw, se));
	}

	private static double qVal(int x, int z) {
		int zIndex = z % 32;
		if (zIndex < 0) {
			zIndex += 32;
		}

		int index = (int) (x + (q[zIndex])) % 32;
		if (index < 0) {
			index += 32;
		}

		return q[index] * 0.03125D;
	}

	private static double lerp(double progress, double start, double end) {
		return start + progress * (end - start);
	}

	private static int floor(double value) {
		int i = (int)value;
		return value < (float)i ? i - 1 : i;
	}

	private static int ceil(double value) {
		int i = (int)value;
		return value > (double)i ? i + 1 : i;
	}

	private static double fade(double n) {
		return n * n * (3 - (n * 2));
	}

	@Override
	public int implementedFunctions() {
		return NoiseImplementation.NOISE_2D;
	}
}