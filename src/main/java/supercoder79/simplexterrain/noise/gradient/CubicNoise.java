package supercoder79.simplexterrain.noise.gradient;

import supercoder79.simplexterrain.api.noise.Noise;
import supercoder79.simplexterrain.api.noise.NoiseImplementation;

public class CubicNoise extends Noise {
	private static final int RND_A = 134775813;
	private static final int RND_B = 1103515245;

	private long seed;
	private int octave;
	private int periodx = (int)Math.pow(2, 12);
	private int periody = (int)Math.pow(2, 12);

//	public CubicNoise(final int seed, final int octave, final int periodx, final int periody) {
//		this(seed);
//
//		this.periodx = periodx;
//		this.periody = periody;
//	}

	public CubicNoise(long seed) {
		super(seed);
		this.seed = seed;
		this.octave = 1;
	}

	public double sample(final double x) {
		final int xi = (int) Math.floor(x / octave);
		final double lerp = x / octave - xi;

		return interpolate(
				randomize(seed, tile(xi - 1, periodx), 0),
				randomize(seed, tile(xi, periodx), 0),
				randomize(seed, tile(xi + 1, periodx), 0),
				randomize(seed, tile(xi + 2, periodx), 0),
				lerp) * 0.5f + 0.25f;
	}

	public double sample(double x, double y) {
		x *= 0.5;
		y *= 0.5;
		final int xi = (int) Math.floor(x / octave);
		final double lerpx = x / octave - xi;
		final int yi = (int) Math.floor(y / octave);
		final double lerpy = y / octave - yi;

		double[] xSamples = new double[4];

		for(int i = 0; i < 4; ++i)
			xSamples[i] = interpolate(
					randomize(seed, tile(xi - 1, periodx), tile(yi - 1 + i, periody)),
					randomize(seed, tile(xi, periodx), tile(yi - 1 + i, periody)),
					randomize(seed, tile(xi + 1, periodx), tile(yi - 1 + i, periody)),
					randomize(seed, tile(xi + 2, periodx), tile(yi - 1 + i, periody)),
					lerpx);
		double d = (interpolate(xSamples[0], xSamples[1], xSamples[2], xSamples[3], lerpy) * 0.25f + 0.25f);
		d /= Math.pow(10, 9) / 2;
//		System.out.println(d);
		return d;
	}

	@Override
	public double sample(double x, double y, double z) {
		return 0;
	}

	private static double randomize(final long seed, final int x, final int y) {
		return (float) ((((x ^ y) * RND_A) ^ (seed + x)) * (((RND_B * x) << 16) ^ (RND_B * y) - RND_A)) / Integer.MAX_VALUE;
	}

	private static int tile(final int coordinate, final int period) {
		return coordinate % period;
	}

	private static double interpolate(final double a, final double b, final double c, final double d, final double x) {
		final double p = (d - c) - (a - b);

		return x * (x * (x * p + ((a - b) - p)) + (c - a)) + b;
	}

	@Override
	public int implementedFunctions() {
		return NoiseImplementation.NOISE_2D;
	}
}