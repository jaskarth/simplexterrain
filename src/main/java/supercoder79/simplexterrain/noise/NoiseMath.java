package supercoder79.simplexterrain.noise;

import supercoder79.simplexterrain.api.noise.Noise;

public class NoiseMath {
    private static final double CONST = 0.1;

    // Derivative code

    public static double derivative(Noise sampler, double x, double z) {
        double baseSample = sigmoid(sampler.sample(x, z));

        double xVal1 = sigmoid(sampler.sample(x + CONST, z) - baseSample) / CONST;
        double xVal2 = sigmoid(sampler.sample(x - CONST, z) - baseSample) / CONST;
        double zVal1 = sigmoid(sampler.sample(x, z + CONST) - baseSample) / CONST;
        double zVal2 = sigmoid(sampler.sample(x, z - CONST) - baseSample) / CONST;

        return Math.sqrt(((xVal2 - xVal1) * (xVal2 - xVal1)) + ((zVal2 - zVal1) * (zVal2 - zVal1)));
    }

    public static double derivative2(Noise sampler, double x, double z) {
        double a = 0.788675134594813 * CONST;
        double b = 0.211324865405187 * CONST;
        double c = 0.577350269189626 * CONST;

        double v1 = sigmoid(sampler.sample(x + a, z - b));
        double v2 = sigmoid(sampler.sample(x - b, z + a));
        double v3 = sigmoid(sampler.sample(x - c, z - c));

        double dx = (v1 * a + v2 * -b + v3 * -c) * (0.40824829046386296 / CONST);
        double dy = (v1 * -b + v2 * a + v3 * -c) * (0.40824829046386296 / CONST);

        return Math.sqrt(dx*dx + dy*dy);
    }

    public static double derivative(Noise sampler, double x, double y, double z) {
        double baseSample = sigmoid(sampler.sample(x, y, z));

        double xVal1 = sigmoid(sampler.sample(x + CONST, y, z) - baseSample) / CONST;
        double xVal2 = sigmoid(sampler.sample(x - CONST, y, z) - baseSample) / CONST;
        double zVal1 = sigmoid(sampler.sample(x, y, z + CONST) - baseSample) / CONST;
        double zVal2 = sigmoid(sampler.sample(x, y, z - CONST) - baseSample) / CONST;
        double yVal1 = sigmoid(sampler.sample(x, y + CONST, z) - baseSample) / CONST;
        double yVal2 = sigmoid(sampler.sample(x, y - CONST, z) - baseSample) / CONST;

        return Math.sqrt(((xVal2 - xVal1) * (xVal2 - xVal1)) + ((zVal2 - zVal1) * (zVal2 - zVal1)) + ((yVal2 - yVal1) * (yVal2 - yVal1)));
    }

    //Utilities

    // \frac{256}{e^{\frac{7}{3}-\frac{x}{64}}+1}
    public static double sigmoid(double val) {
        return 256 / (Math.exp(7 / 3f - val / 64) + 1);
    }
}