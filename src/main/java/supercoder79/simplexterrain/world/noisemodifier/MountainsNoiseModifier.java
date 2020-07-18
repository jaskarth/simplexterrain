package supercoder79.simplexterrain.world.noisemodifier;

import java.nio.file.Paths;

import net.minecraft.world.gen.ChunkRandom;
import supercoder79.simplexterrain.SimplexTerrain;
import supercoder79.simplexterrain.api.noise.Noise;
import supercoder79.simplexterrain.api.noise.OctaveNoiseSampler;
import supercoder79.simplexterrain.api.noisemodifier.NoiseModifier;
import supercoder79.simplexterrain.configs.ConfigUtil;
import supercoder79.simplexterrain.configs.noisemodifiers.MountainConfigData;

public class MountainsNoiseModifier implements NoiseModifier {
	private MountainConfigData config;
	private OctaveNoiseSampler<? extends Noise> mountainNoise;

	@Override
	public void init(long seed) {
		mountainNoise = new OctaveNoiseSampler<>(SimplexTerrain.CONFIG.noiseGenerator.noiseClass, new ChunkRandom(seed + 20), config.octaves, config.frequency, config.amplitudeHigh, config.amplitudeLow);
	}

	@Override
	public void setup() {
		config = ConfigUtil.getFromConfig(MountainConfigData.class, Paths.get("config", "simplexterrain", "noisemodifiers", "mountains.json"));
	}

	@Override
	public double modify(int x, int z, double currentNoiseValue) {
		return currentNoiseValue + Math.max(mountainNoise.sample(x, z), 0);
	}

	@Override
	public String getName() {
		return "MOUNTAINS";
	}
}
