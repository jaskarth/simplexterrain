package supercoder79.simplexterrain.world.noisemodifier;

import supercoder79.simplexterrain.api.cache.CacheSampler;
import supercoder79.simplexterrain.api.noise.NoiseModifier;
import supercoder79.simplexterrain.configs.ConfigUtil;
import supercoder79.simplexterrain.configs.noisemodifiers.MesaConfigData;
import supercoder79.simplexterrain.noise.gradient.OpenSimplexNoise;

import java.nio.file.Paths;

public final class MesaNoiseModifier extends NoiseModifier {
	private MesaConfigData config;

	private CacheSampler cutoffSampler;
	private CacheSampler terraceSampler;

	public MesaNoiseModifier() {
		super(8L);
	}

	@Override
	public void init(long seed) {
		this.cutoffSampler = new CacheSampler(this.createNoiseSampler(OpenSimplexNoise.class, config.cutoffOctaves, config.cutoffFrequency));
		this.terraceSampler = new CacheSampler(this.createNoiseSampler(OpenSimplexNoise.class, config.terraceOctaves, config.terraceFrequency));
	}

	@Override
	public void setup() {
		config = ConfigUtil.getFromConfig(MesaConfigData.class, Paths.get("config", "simplexterrain", "noisemodifiers", "mesas.json"));
	}

	@Override
	public double modify(int x, int z, double currentNoiseValue, double scaleValue) {
		if (scaleValue < config.maximumScale) {
			if (this.cutoffSampler.sample(x, z) > config.noiseCutoff) {
				return currentNoiseValue + getTerraceHeightOffset(terraceSampler.sample(x, z));
			}
		}

		return currentNoiseValue;
	}

	private double getTerraceHeightOffset(double noise) {
		if (noise < config.minTerraceThreshold) {
			return config.minTerraceHeight;
		} else if (noise < config.maxTerraceThreshold) {
			return config.maxTerraceHeight;
		} else {
			return config.defaultTerraceHeight;
		}
	}
}
