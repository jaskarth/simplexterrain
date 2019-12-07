package supercoder79.simplexterrain.noise.value;

import net.minecraft.util.math.MathHelper;
import supercoder79.simplexterrain.api.noise.Noise;

/**
 * @author Valoeghese
 */
public final class ValueNoise extends Noise {
	private final SimpleRandom rand;
	private final boolean fade = true;

	private final double offsetX;
	private final double offsetY;
	private final double offsetZ;

	public ValueNoise(long seed) {
		super(seed);
		rand = new SimpleRandom(seed);
		rand.init(0, 0);
		offsetX = rand.randomDouble();
		offsetY = rand.randomDouble();
		offsetZ = rand.randomDouble();
	}

	private double keyPoint(double x, double y) {
		return (2 * rand.randomDouble(x, y)) - 1;
	}

	private double keyPoint(double x, double y, double z) {
		return (2 * rand.randomDouble(x, y, z)) - 1;
	}

	private double sampleImpl(double x, double y) {
		double xFloor = MathHelper.floor(x);
		double yFloor = MathHelper.floor(y);

		double localX = x - xFloor;
		double localY = y - yFloor;

		if (fade) {
			localX = fade(localX);
			localY = fade(localY);
		}

		double NW = keyPoint(xFloor, yFloor + 1);
		double NE = keyPoint(xFloor + 1, yFloor + 1);
		double SW = keyPoint(xFloor, yFloor);
		double SE = keyPoint(xFloor + 1, yFloor);

		return MathHelper.lerp(localY, 
				MathHelper.lerp(localX, SW, SE),
				MathHelper.lerp(localX, NW, NE));
	}

	private double sampleImpl(double x, double y, double z) {
		double xFloor = MathHelper.floor(x);
		double yFloor = MathHelper.floor(y);
		double zFloor = MathHelper.floor(z);

		double localX = x - xFloor;
		double localY = y - yFloor;
		double localZ = z - zFloor;

		if (fade) {
			localX = fade(localX);
			localY = fade(localY);
			localZ = fade(localZ);
		}

		double NWLow = keyPoint(xFloor, yFloor + 1, zFloor);
		double NELow = keyPoint(xFloor + 1, yFloor + 1, zFloor);
		double SWLow = keyPoint(xFloor, yFloor, zFloor);
		double SELow = keyPoint(xFloor + 1, yFloor, zFloor);

		double NWHigh = keyPoint(xFloor, yFloor + 1, zFloor + 1);
		double NEHigh = keyPoint(xFloor + 1, yFloor + 1, zFloor + 1);
		double SWHigh = keyPoint(xFloor, yFloor, zFloor + 1);
		double SEHigh = keyPoint(xFloor + 1, yFloor, zFloor + 1);

		return MathHelper.lerp(localZ, 
				MathHelper.lerp(localY, 
						MathHelper.lerp(localX, SWLow, SELow),
						MathHelper.lerp(localX, NWLow, NELow)), 
				MathHelper.lerp(localY, 
						MathHelper.lerp(localX, SWHigh, SEHigh),
						MathHelper.lerp(localX, NWHigh, NEHigh)));
	}

	private static double fade(double n) {
		return n * n * n * (n * (n * 6 - 15) + 10);
	}

	@Override
	public double sample(double x, double y) {
		return sampleImpl(x + offsetX, y + offsetY);
	}

	@Override
	public double sample(double x, double y, double z) {
		return sampleImpl(x + offsetX, y + offsetY, z + offsetZ);
	}
}

class SimpleRandom {
	private long seed;
	private long localSeed;

	public SimpleRandom(long seed) {
		this.seed = seed;
		this.localSeed = seed;
	}

	public int random(double x, double y, int bound) {
		init(x, y);

		int result = (int) ((localSeed >> 24) % bound);
		if (result < 0) {
			result += (bound - 1);
		}

		return result;
	}

	public int random(double x, double y, double z, int bound) {
		init(x, y, z);

		int result = (int) ((localSeed >> 24) % bound);
		if (result < 0) {
			result += (bound - 1);
		}

		return result;
	}

	public double randomDouble(double x, double y) {
		return (double) this.random(x, y, Integer.MAX_VALUE) / (double) Integer.MAX_VALUE;
	}

	public double randomDouble(double x, double y, double z) {
		return (double) this.random(x, y, z, Integer.MAX_VALUE) / (double) Integer.MAX_VALUE;
	}

	public int random(int bound) {
		int result = (int) ((localSeed >> 24) % bound);
		if (result < 0) {
			result += (bound - 1);
		}

		localSeed += seed;
		localSeed *= 3412375462423L * localSeed + 834672456235L;

		return result;
	}

	public double randomDouble() {
		return (double) this.random(Integer.MAX_VALUE) / (double) Integer.MAX_VALUE;
	}

	public void init(double x, double y) {
		localSeed = seed;
		localSeed += (x * 72624D);
		localSeed *= 3412375462423L * localSeed + 834672456235L;
		localSeed += (y * 8963D);
		localSeed *= 3412375462423L * localSeed + 834672456235L;
	}

	public void init(double x, double y, double z) {
		localSeed = seed;
		localSeed += (x * 72624D);
		localSeed *= 3412375462423L * localSeed + 834672456235L;
		localSeed += (y * 8963D);
		localSeed *= 3412375462423L * localSeed + 834672456235L;
		localSeed += (z * 56385D);
		localSeed *= 3412375462423L * localSeed + 834672456235L;
	}
}