package supercoder79.simplexterrain.world.noisemodifier;

import supercoder79.simplexterrain.SimplexTerrain;
import supercoder79.simplexterrain.api.noise.NoiseModifier;
import supercoder79.simplexterrain.api.noise.OctaveNoiseSampler;
import supercoder79.simplexterrain.noise.gradient.OpenSimplexNoise;

public class FjordNoiseModifier extends NoiseModifier {
	private OctaveNoiseSampler stackNoise;
	private OpenSimplexNoise noiseSampler;

	protected FjordNoiseModifier() {
		super(2L);
	}

	@Override
	public void init(long seed) {
		noiseSampler = new OpenSimplexNoise(seed - 30);
		stackNoise = this.createNoiseSampler(OpenSimplexNoise.class, 4, 250, 45, 32);
	}

	@Override
	public void setup() {
	}

	@Override
	public double modify(int x, int z, double currentNoiseValue, double scaleValue) {
		double noise = noiseSampler.sample((double) x / 980, (double) z / 980);

		double dist = Math.abs(noise - 0.13);

		if (currentNoiseValue > (SimplexTerrain.CONFIG.seaLevel + 3) - SimplexTerrain.CONFIG.baseHeight) // don't make the fjord cursed
		if (dist <= 0.04) {
			currentNoiseValue = -60 + ((dist*500) + 30);
		}
		return currentNoiseValue;
	}
}
