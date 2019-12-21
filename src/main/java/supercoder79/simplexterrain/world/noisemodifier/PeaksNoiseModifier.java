package supercoder79.simplexterrain.world.noisemodifier;

import net.minecraft.world.gen.ChunkRandom;
import supercoder79.simplexterrain.SimplexTerrain;
import supercoder79.simplexterrain.api.noise.NoiseModifier;
import supercoder79.simplexterrain.api.noise.OctaveNoiseSampler;
import supercoder79.simplexterrain.noise.gradient.OpenSimplexNoise;

public class PeaksNoiseModifier implements NoiseModifier {
	private OctaveNoiseSampler peaksNoise;

	@Override
	public void init(long seed) {
		peaksNoise = new OctaveNoiseSampler<>(OpenSimplexNoise.class, new ChunkRandom(seed), SimplexTerrain.CONFIG.peaksOctaveAmount, SimplexTerrain.CONFIG.peaksFrequency, 1.0, 1.0);
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
