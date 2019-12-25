package supercoder79.simplexterrain.world.noisemodifier;

import net.minecraft.world.gen.ChunkRandom;
import supercoder79.simplexterrain.api.noise.NoiseModifier;
import supercoder79.simplexterrain.api.noise.OctaveNoiseSampler;
import supercoder79.simplexterrain.configs.ConfigUtil;
import supercoder79.simplexterrain.configs.noisemodifiers.SandbarConfigData;
import supercoder79.simplexterrain.noise.gradient.OpenSimplexNoise;

import java.nio.file.Paths;

public class SandbarNoiseModifier extends NoiseModifier {
	private SandbarConfigData config;

	private ChunkRandom random = new ChunkRandom(0);
	private OctaveNoiseSampler stackNoise;

	protected SandbarNoiseModifier() {
		super(3L);
	}

	@Override
	public void init(long seed) {
		random.setSeed(seed);
		stackNoise = new OctaveNoiseSampler<>(OpenSimplexNoise.class, random, config.octaves, config.frequency, config.amplitudeHigh, config.amplitudeLow);
	}

	@Override
	public void setup() {
		config = ConfigUtil.getFromConfig(SandbarConfigData.class, Paths.get("config", "simplexterrain", "noisemodifiers", "sandbars.json"));
	}

	@Override
	public double modify(int x, int z, double currentNoiseValue, double scaleValue) {
		double noise = 0;
		if (currentNoiseValue > -55 && currentNoiseValue < -40) {
			double sample = stackNoise.sample(x, z);
			if (sample > config.threshold) noise = sample;
		}
		return currentNoiseValue + noise;
	}
}
