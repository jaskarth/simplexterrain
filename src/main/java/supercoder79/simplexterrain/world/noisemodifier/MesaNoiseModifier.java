package supercoder79.simplexterrain.world.noisemodifier;

import net.minecraft.world.gen.ChunkRandom;
import supercoder79.simplexterrain.api.noise.NoiseModifier;
import supercoder79.simplexterrain.api.noise.OctaveNoiseSampler;
import supercoder79.simplexterrain.noise.gradient.OpenSimplexNoise;

public final class MesaNoiseModifier implements NoiseModifier {
	private static final double noiseCutoff = 0.3;
	private OctaveNoiseSampler<OpenSimplexNoise> cutoffSampler;
	private OctaveNoiseSampler<OpenSimplexNoise> terraceSampler;

	@Override
	public void init(long seed) {
		cutoffSampler = new OctaveNoiseSampler<>(OpenSimplexNoise.class, new ChunkRandom(seed + 8), 3, 1000, 1, 1);
		terraceSampler = new OctaveNoiseSampler<>(OpenSimplexNoise.class, new ChunkRandom(seed), 1, 100, 1, 1);
	}

	@Override
	public void setup() {
	}

	@Override
	public double modify(int x, int z, double currentNoiseValue, double scaleValue) {
		if (scaleValue < 8) {
			if (cutoffSampler.sample(x, z) > noiseCutoff) {
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
