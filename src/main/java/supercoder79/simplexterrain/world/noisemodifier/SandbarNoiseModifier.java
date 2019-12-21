package supercoder79.simplexterrain.world.noisemodifier;

import net.minecraft.world.gen.ChunkRandom;
import supercoder79.simplexterrain.SimplexTerrain;
import supercoder79.simplexterrain.api.noise.NoiseModifier;
import supercoder79.simplexterrain.api.noise.OctaveNoiseSampler;
import supercoder79.simplexterrain.noise.gradient.OpenSimplexNoise;

public class SandbarNoiseModifier extends NoiseModifier {
	private ChunkRandom random = new ChunkRandom(0);
	private OctaveNoiseSampler stackNoise;

	protected SandbarNoiseModifier() {
		super(3L);
	}

	@Override
	public void init(long seed) {
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
