package supercoder79.simplexterrain.noise.value;

import java.util.Random;

import supercoder79.simplexterrain.api.noise.Noise;
import supercoder79.simplexterrain.api.noise.NoiseImplementation;

/**
 * @author Valoeghese
 */
public class ValueNoise extends Noise {
	public ValueNoise(long seed) {
		super(seed);
		Random initRand = new Random(seed);

		// Scamble code. Thanks mojang :P
		for(int index = 0; index < 256; ++index) {
			int newIndexOffset = initRand.nextInt(256 - index);
			int oldValue = this.q[index];
			this.q[index] = this.q[newIndexOffset + index];
			this.q[newIndexOffset + index] = oldValue;
		}
	}

	private int[] q = {
			0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
			21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
			41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60,
			61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80,
			81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100,
			101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120,
			121, 122, 123, 124, 125, 126, 127, 128, 129, 130, 131, 132, 133, 134, 135, 136, 137, 138, 139, 140,
			141, 142, 143, 144, 145, 146, 147, 148, 149, 150, 151, 152, 153, 154, 155, 156, 157, 158, 159, 160,
			161, 162, 163, 164, 165, 166, 167, 168, 169, 170, 171, 172, 173, 174, 175, 176, 177, 178, 179, 180,
			181, 182, 183, 184, 185, 186, 187, 188, 189, 190, 191, 192, 193, 194, 195, 196, 197, 198, 199, 200,
			201, 202, 203, 204, 205, 206, 207, 208, 209, 210, 211, 212, 213, 214, 215, 216, 217, 218, 219, 220,
			221, 222, 223, 224, 225, 226, 227, 228, 229, 230, 231, 232, 233, 234, 235, 236, 237, 238, 239, 240,
			241, 242, 243, 244, 245, 246, 247, 248, 249, 250, 251, 252, 253, 254, 255};

	private static final double ONE_HUNDREDTWENTYEIGHTH = (1.0D / 128.0D);
	private final boolean fade = true;

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
		if (fade) {
			x = fade(x);
			z = fade(z);
		}

		double nw = qVal(x0, z0);
		double ne = qVal(x1, z0);
		double sw = qVal(x0, z1);
		double se = qVal(x1, z1);

		return lerp(
				z,
				lerp(x, nw, ne),
				lerp(z, sw, se));
	}

	private double qVal(int x, int z) {
		int index = (x & 0x7F) + (z & 0x7F);
		return ((double) q[index] * ONE_HUNDREDTWENTYEIGHTH) - 1.0;
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