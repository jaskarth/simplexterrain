package supercoder79.simplexterrain.noise.inprogress;

import supercoder79.simplexterrain.api.noise.Noise;

import java.util.Random;

/**
 * Please excuse the really weird code here. It was ported over from c++.
 *
 * @author SirMack, SuperCoder79
 */
public class GaborNoise extends Noise {
	InternalNoise internalNoise;

	public GaborNoise(long seed) {
		super(seed);
		internalNoise = new InternalNoise(1.0f, 0.05f, 0.625f, (float) (Math.PI / 4.f), 64f, 256, System.currentTimeMillis());
	}

	@Override
	public double sample(double x, double z) {
		return internalNoise.operator((float) x,(float) z);
	}

	@Override
	public double sample(double x, double y, double z) {
		return 0;
	}

	static class PRNG {
		Random r;
		long x_;

		void seed(long s) {
			r = new Random(s);
//            x_ = s;
		}

		long operator() {
			return r.nextLong();
//            x_ *= 3039177861L;
//            return x_;
		}

		float uniform_0_1() {
			return (float) (operator()) / (float) (Long.MAX_VALUE);
		}

		float uniform(float min, float max) {
			return min + (uniform_0_1() * (max - min));
		}

		long poisson(float mean) {
			float g_ = (float) Math.exp(-mean);
			long em = 0;
			double t = uniform_0_1();
			while (t > g_) {
				++em;
				t *= uniform_0_1();
			}
			return em;
		}

//        long x_;
	}

	static float gabor(float K, float a, float F_0, float omega_0, float x, float y) {
		float gaussian_envelop = (float) (K * Math.exp(-Math.PI * (a * a) * ((x * x) + (y * y))));
		float sinusoidal_carrier = (float) Math.cos(2.0 * Math.PI * F_0 * ((x * Math.cos(omega_0)) + (y * Math.sin(omega_0))));
		return gaussian_envelop * sinusoidal_carrier;
	}

	static long morton(long x, long y) {
		Random r = new Random(31 * x + y);
		return r.nextLong();
//        long z = 0;
//        for (long i = 0; i < Long.SIZE; ++i) {
//            z |= ((x & (1 << i)) << i) | ((y & (1 << i)) << (i + 1));
//        }
//        System.out.println(x+" "+y+" => "+z);
//        return z;
	}

	static class InternalNoise {
		InternalNoise(float K, float a, float F_0, float omega_0, float number_of_impulses_per_kernel, long period,
					  long random_offset) {
			K_ = K;
			a_ = a;
			F_0_ = F_0;
			omega_0_ = omega_0;
			period_ = period;
			random_offset_ = random_offset;
			kernel_radius_ = (float) (Math.sqrt(-Math.log(0.05) / Math.PI) / a_);
			impulse_density_ = (float) (number_of_impulses_per_kernel / (Math.PI * kernel_radius_ * kernel_radius_));
		}

		float operator(float x, float y) {
//            System.out.println(x+" "+y);
//            return cell(23,13,x/256.0f,y/256.0f);
			x /= kernel_radius_;
			y /= kernel_radius_;
			float int_x = (float) Math.floor(x), int_y = (float) Math.floor(y);
			float frac_x = x - int_x, frac_y = y - int_y;
			int i = (int) (int_x), j = (int) (int_y);
			float noise = 0;
			for (int di = -1; di <= +1; ++di) {
				for (int dj = -1; dj <= +1; ++dj) {
					noise += cell(i + di, j + dj, frac_x - di, frac_y - dj);
				}
			}
			return noise;
		}

		float cell(int i, int j, float x, float y) {
//			long s = (((long(j) % period_) * period_) + (long(i) % period_)) + random_offset_; // periodic noise
			long s = morton(i, j) + random_offset_; // nonperiodic noise
//            if(Math.sqrt(5)>)return (float)s/Long.MAX_VALUE;
			if (s == 0) s = 1;
			PRNG prng = new PRNG();
			prng.seed(s);
			double number_of_impulses_per_cell = impulse_density_ * kernel_radius_ * kernel_radius_;
			long number_of_impulses = prng.poisson((float) number_of_impulses_per_cell);
			float noise = 0;
			for (long __ = 0; __ < number_of_impulses; ++__) {
				float x_i = prng.uniform_0_1();
				float y_i = prng.uniform_0_1();
				x_i = y_i = 0;
				float w_i = prng.uniform(-1, +1);
				float omega_0_i = prng.uniform(0.0f, (float) (2.0 * Math.PI));
				float x_i_x = x - x_i;
				float y_i_y = y - y_i;
				if (((x_i_x * x_i_x) + (y_i_y * y_i_y)) < 20.0) {
//                    noise += w_i *
//                            gabor(K_, a_, F_0_, omega_0_, x_i_x * kernel_radius_, y_i_y * kernel_radius_); // anisotropic
					noise += w_i * gabor(K_, a_, F_0_, omega_0_i, x_i_x * kernel_radius_, y_i_y * kernel_radius_); // isotropic
				}
			}
			return noise;
		}

		float variance() {
			float integral_gabor_filter_squared =
					(float) (((K_ * K_) / (4.0 * a_ * a_)) * (1.0 + Math.exp(-(2.0 * Math.PI * F_0_ * F_0_) / (a_ * a_))));
			return (float) (impulse_density_ * (1.0 / 3.0) * integral_gabor_filter_squared);
		}

		float K_;
		float a_;
		float F_0_;
		float omega_0_;
		float kernel_radius_;
		float impulse_density_;
		long period_;
		long random_offset_;
	}
}