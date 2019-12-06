package supercoder79.simplexterrain.noise.inprogress;

import supercoder79.simplexterrain.api.noise.Noise;
import supercoder79.simplexterrain.api.noise.NoiseImplementation;

/**
 * Shitty Gabor noise implementation
 * It doesn't work
 * Don't use this garbage
 * @author SuperCoder79
 */

public class GaborNoise extends Noise {
	private long random_offset_;

	private static class PRNG {
		long x;

		void seed(long x) {
			this.x = x;
		}

		long operator() {
			x *= 3039177861L;
			return x;
		}

		double uniform_0_1() { return operator() / 4294967295.d; }

		double uniform(double min, double max) {
			return min + (uniform_0_1() * (max - min));
		}

		long poisson(double mean)
		{
			double g_ = Math.exp(-mean);
			long em = 0;
			double t = uniform_0_1();
			while (t > g_) {
				++em;
				t *= uniform_0_1();
			}
			return em;
		}

	}

	private double kernel_radius_;
	private double impulse_density_;
	private double number_of_impulses_per_kernel = 64.0;
	double K_ = 1.0;
	double a_ = 0.05;
	double F_0_ = 0.0625;
	double omega_0_ = Math.PI / 4;

	public GaborNoise(long seed) {
		super(seed);
		this.random_offset_ = seed;
		kernel_radius_ = Math.sqrt(-Math.log(0.05) / Math.PI) / a_;
		impulse_density_ = number_of_impulses_per_kernel / (Math.PI * kernel_radius_ * kernel_radius_);
	}

	long morton(long x, long y) {
		long z = 0;
		//sizeof unsigned * char_bit
		//4 * 8
		for (long i = 0; i < (32); ++i) {
			z |= ((x & (1 << i)) << i) | ((y & (1 << i)) << (i + 1));
		}
		return z;
	}

	double gabor(double K, double a, double F0, double omega0, double x, double y) {
		return 0;
	}

	double cell(long i, long j, double x, double z) {
		long l = morton(i, j);
		System.out.println(l);
		return 0;
	}


	public double sample(double x, double z) {
		PRNG prng = new PRNG();
		prng.poisson(2);
		x /= kernel_radius_; z /= kernel_radius_;
		double int_x = Math.floor(x), int_z = Math.floor(z);
		double frac_x = x - int_x, frac_y = z - int_z;
		int i = (int)int_x, j = (int)int_z;
		float noise = 0;
		for (int di = -1; di <= +1; ++di) {
			for (int dj = -1; dj <= +1; ++dj) {
				noise += cell(i + di, j + dj, frac_x - di, frac_y - dj);
			}
		}
		return 0;
	}

	double variance() {
		double integral_gabor_filter_squared = ((K_ * K_) / (4.0 * a_ * a_)) * (1.0 + Math.exp(-(2.0 * Math.PI * F_0_ * F_0_) / (a_ * a_)));
		return impulse_density_ * (1.0 / 3.0) * integral_gabor_filter_squared;
	}

	@Override
	public double sample(double x, double y, double z) {
		return 0;
	}

	@Override
	public int implementedFunctions() {
		return NoiseImplementation.NOISE_2D;
	}
}
