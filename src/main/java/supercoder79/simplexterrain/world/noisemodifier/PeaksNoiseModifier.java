package supercoder79.simplexterrain.world.noisemodifier;

import supercoder79.simplexterrain.SimplexTerrain;
import supercoder79.simplexterrain.api.cache.CacheSampler;
import supercoder79.simplexterrain.api.noise.NoiseModifier;
import supercoder79.simplexterrain.api.noise.OctaveNoiseSampler;
import supercoder79.simplexterrain.noise.gradient.OpenSimplexNoise;

public class PeaksNoiseModifier extends NoiseModifier {
	public PeaksNoiseModifier() {
		super(0L);
	}

	private CacheSampler peaksNoise;

	@Override
	public void init(long seed) {
		this.peaksNoise = new CacheSampler(this.createNoiseSampler(OpenSimplexNoise.class, SimplexTerrain.CONFIG.peaksOctaveAmount, SimplexTerrain.CONFIG.peaksFrequency));
	}

	@Override
	public void setup() {
	}

	@Override
	public double modify(int x, int z, double currentNoiseValue, double scaleValue) {
		return currentNoiseValue + modifyPeaksNoise(this.peaksNoise.sample(x, z));
	}

	private double modifyPeaksNoise(double sample) {
		sample += SimplexTerrain.CONFIG.peaksSampleOffset;
		if (sample < 0) {
			return 0;
		} else {
			return sample * SimplexTerrain.CONFIG.peaksAmplitude;
		}
	}
}
