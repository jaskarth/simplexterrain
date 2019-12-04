package supercoder79.simplexterrain.api.noise;

/**
 * Gets which functions are implemented properly in noise. This is used to ensure that the noise class can actually perform
 * a specified task.
 */
public class NoiseImplementation {
	public static final int NOISE_2D = 1;
	public static final int NOISE_3D = 2;
	public static final int NOISE_4D = 4;
	public static final int GOOD_ENOUGH = NOISE_2D | NOISE_3D;
	public static final int ALL = NOISE_2D | NOISE_3D | NOISE_4D;

	public static boolean is2DNoiseImplemented(int noise) {
		return (noise & NOISE_2D) == NOISE_2D;
	}

	public static boolean is3DNoiseImplemented(int noise) {
		return (noise & NOISE_3D) == NOISE_3D;
	}

	public static boolean is4DNoiseImplemented(int noise) {
		return (noise & NOISE_4D) == NOISE_4D;
	}
}
