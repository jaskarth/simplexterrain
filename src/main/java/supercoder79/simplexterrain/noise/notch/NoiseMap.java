package supercoder79.simplexterrain.noise.notch;

import supercoder79.simplexterrain.api.noise.Noise;

import java.util.*;

public class NoiseMap extends Noise {
    private Random random;
    private int levels;
    private int fuzz;

    public NoiseMap(long seed) {
        super(seed);
        this.random = new Random(seed);
        this.fuzz = 16;
        this.levels = 1;
    }

    public int[] read(final int width, final int height) {
        final int[] tmp = new int[width * height];
        final int level = this.levels;
        for (int step = width >> level, y = 0; y < height; y += step) {
            for (int x = 0; x < width; x += step) {
                tmp[x + y * width] = (random.nextInt(256) - 128) * this.fuzz;
            }
        }
        for (int step = width >> level; step > 1; step /= 2) {
            final int val = 256 * (step << level);
            final int ss = step / 2;
            for (int y2 = 0; y2 < height; y2 += step) {
                for (int x2 = 0; x2 < width; x2 += step) {
                    final int ul = tmp[(x2 + 0) % width + (y2 + 0) % height * width];
                    final int ur = tmp[(x2 + step) % width + (y2 + 0) % height * width];
                    final int dl = tmp[(x2 + 0) % width + (y2 + step) % height * width];
                    final int dr = tmp[(x2 + step) % width + (y2 + step) % height * width];
                    final int m = (ul + dl + ur + dr) / 4 + random.nextInt(val * 2) - val;
                    tmp[x2 + ss + (y2 + ss) * width] = m;
                }
            }
            for (int y2 = 0; y2 < height; y2 += step) {
                for (int x2 = 0; x2 < width; x2 += step) {
                    final int c = tmp[x2 + y2 * width];
                    final int r = tmp[(x2 + step) % width + y2 * width];
                    final int d = tmp[x2 + (y2 + step) % width * width];
                    final int mu = tmp[(x2 + ss & width - 1) + (y2 + ss - step & height - 1) * width];
                    final int ml = tmp[(x2 + ss - step & width - 1) + (y2 + ss & height - 1) * width];
                    final int i = tmp[(x2 + ss) % width + (y2 + ss) % height * width];
                    final int u = (c + r + i + mu) / 4 + random.nextInt(val * 2) - val;
                    final int l = (c + d + i + ml) / 4 + random.nextInt(val * 2) - val;
                    tmp[x2 + ss + y2 * width] = u;
                    tmp[x2 + (y2 + ss) * width] = l;
                }
            }
        }
        final int[] result = new int[width * height];
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                result[x + y * width] = tmp[x % width + y % height * width] / 512 + 128;
            }
        }
        return result;
    }

    @Override
    public double sample(double x, double z) {
        return read(16, 16)[0] / 500.0;
    }

    @Override
    public double sample(double x, double y, double z) {
        return 0;
    }
}
