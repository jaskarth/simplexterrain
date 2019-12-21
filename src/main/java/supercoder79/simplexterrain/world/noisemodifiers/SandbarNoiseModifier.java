package supercoder79.simplexterrain.world.noisemodifiers;

import net.minecraft.world.gen.ChunkRandom;
import supercoder79.simplexterrain.SimplexTerrain;
import supercoder79.simplexterrain.api.noise.NoiseModifier;
import supercoder79.simplexterrain.api.noise.OctaveNoiseSampler;
import supercoder79.simplexterrain.noise.gradient.OpenSimplexNoise;

public class SandbarNoiseModifier implements NoiseModifier {
	private ChunkRandom random = new ChunkRandom(0);
	private OctaveNoiseSampler stackNoise;

	@Override
	public void init(long seed) {
		random.setSeed(seed);
		stackNoise = new OctaveNoiseSampler<>(OpenSimplexNoise.class, random, 4, 250, 45, 32);
	}

	@Override
	public void setup() {

	}

	@Override
	public double modify(int x, int z, double currentNoiseValue, double scaleValue) {
		double noise = 0;
		if (currentNoiseValue > -55 && currentNoiseValue < -40) {
			double sample = stackNoise.sample(x, z);
			if (sample > 0.5) noise = sample;
		}
		return currentNoiseValue + noise;
	}
}
