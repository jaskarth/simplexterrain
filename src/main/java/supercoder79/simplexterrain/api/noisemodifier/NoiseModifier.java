package supercoder79.simplexterrain.api.noisemodifier;

import net.minecraft.world.gen.ChunkRandom;
import supercoder79.simplexterrain.api.noise.Noise;
import supercoder79.simplexterrain.api.noise.OctaveNoiseSampler;

public abstract class NoiseModifier {
	protected abstract void init(long seed);
	public abstract void setup();
	public abstract double modify(int x, int z, double currentNoiseValue, double scaleValue);

	protected NoiseModifier(long salt) {
		this.salt = salt;
	}

	public final void init(long seed, ChunkRandom reuseableRandom) {
		this.reuseableRandom = reuseableRandom;
		reuseableRandom.setSeed(seed + salt);
		this.init(seed);
	}

	protected final <T extends Noise> OctaveNoiseSampler<T> createNoiseSampler(Class<T> clazz, int octaves, double frequency) {
		return this.createNoiseSampler(clazz, octaves, frequency, 1, 1);
	}

	protected final <T extends Noise> OctaveNoiseSampler<T> createNoiseSampler(Class<T> clazz, int octaves, double frequency, double amplitudeHigh, double amplitudeLow) {
		return new OctaveNoiseSampler<T>(clazz, reuseableRandom, octaves, frequency, amplitudeHigh, amplitudeLow);
	}

	protected final void setExactNoiseSeed(long seed) {
		this.reuseableRandom.setSeed(seed);
	}

	protected final void setNoiseSeed(long seed) {
		this.reuseableRandom.setSeed(seed + salt);
	}

	private ChunkRandom reuseableRandom;
	protected final long salt;
}
