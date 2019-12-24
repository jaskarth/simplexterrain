package supercoder79.simplexterrain.world.noisemodifier;

import supercoder79.simplexterrain.api.cache.CacheSampler;
import supercoder79.simplexterrain.api.noise.NoiseModifier;
import supercoder79.simplexterrain.api.noise.OctaveNoiseSampler;
import supercoder79.simplexterrain.noise.gradient.OpenSimplexNoise;

public final class MesaNoiseModifier extends NoiseModifier {
	public MesaNoiseModifier() {
		super(8L);
	}

	private static final double noiseCutoff = 0.3;
	private CacheSampler cutoffSampler;
	private CacheSampler terraceSampler;

	@Override
	public void init(long seed) {
		this.cutoffSampler = new CacheSampler(this.createNoiseSampler(OpenSimplexNoise.class, 3, 1000));
		this.terraceSampler = new CacheSampler(this.createNoiseSampler(OpenSimplexNoise.class, 1, 100));
	}

	@Override
	public void setup() {
	}

	@Override
	public double modify(int x, int z, double currentNoiseValue, double scaleValue) {
		if (scaleValue < 8) {
			if (this.cutoffSampler.sample(x, z) > noiseCutoff) {
				return currentNoiseValue + getTerraceHeightOffset(terraceSampler.sample(x, z));
			}
		}

		return currentNoiseValue;
	}

	private double getTerraceHeightOffset(double noise) {
		if (noise < -0.3) {
			return 14;
		} else if (noise < 0.3) {
			return 23;
		} else {
			return 20;
		}
	}
}
