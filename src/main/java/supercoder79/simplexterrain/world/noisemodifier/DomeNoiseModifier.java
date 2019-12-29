package supercoder79.simplexterrain.world.noisemodifier;

import java.nio.file.Paths;

import supercoder79.simplexterrain.api.cache.CacheSampler;
import supercoder79.simplexterrain.api.noise.NoiseModifier;
import supercoder79.simplexterrain.configs.ConfigUtil;
import supercoder79.simplexterrain.configs.noisemodifiers.DomeConfigData;
import supercoder79.simplexterrain.noise.gradient.OpenSimplexNoise;

public class DomeNoiseModifier extends NoiseModifier {
	private DomeConfigData config;

	public DomeNoiseModifier() {
		super(39L);
	}

	private CacheSampler domeNoise;

	@Override
	public void init(long seed) {
		this.domeNoise = new CacheSampler(this.createNoiseSampler(OpenSimplexNoise.class, config.octaves, config.frequency));
	}

	@Override
	public void setup() {
		config = ConfigUtil.getFromConfig(DomeConfigData.class, Paths.get("config", "simplexterrain", "noisemodifiers", "domes.json"));
	}

	@Override
	public double modify(int x, int z, double currentNoiseValue, double scaleValue) {
		double sample = domeNoise.sample(x, z);
		if (sample > config.replaceThreshold) {
			currentNoiseValue -= sample * config.blocksReplaceAmt;
		}
		return currentNoiseValue;
	}
}
