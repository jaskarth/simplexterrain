package supercoder79.simplexterrain.world.noisemodifier;

import supercoder79.simplexterrain.api.noise.NoiseModifier;
import supercoder79.simplexterrain.api.noise.OctaveNoiseSampler;
import supercoder79.simplexterrain.noise.gradient.OpenSimplexNoise;

public class DomeNoiseModifier extends NoiseModifier {
	public DomeNoiseModifier() {
		super(39L);
	}

	private OctaveNoiseSampler domeNoise;

	@Override
	public void init(long seed) {
		this.domeNoise = this.createNoiseSampler(OpenSimplexNoise.class, 3, 200);
	}

	@Override
	public void setup() {
	}

	@Override
	public double modify(int x, int z, double currentNoiseValue, double scaleValue) {
		double sample = domeNoise.sample(x, z);
		if (sample > 0) {
			currentNoiseValue -= sample * 40;
		}
		return currentNoiseValue;
	}
}
