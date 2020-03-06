package supercoder79.simplexterrain.world.noisemodifier;

import java.nio.file.Paths;

import supercoder79.simplexterrain.SimplexTerrain;
import supercoder79.simplexterrain.api.noisemodifier.NoiseModifier;
import supercoder79.simplexterrain.configs.ConfigUtil;
import supercoder79.simplexterrain.configs.noisemodifiers.FjordConfigData;
import supercoder79.simplexterrain.noise.gradient.OpenSimplexNoise;

public class FjordNoiseModifier extends NoiseModifier {
	private FjordConfigData config;

	private OpenSimplexNoise noiseSampler;

	protected FjordNoiseModifier() {
		super(2L);
	}

	@Override
	public void init(long seed) {
		noiseSampler = new OpenSimplexNoise(seed - 30);
	}

	@Override
	public void setup() {
		config = ConfigUtil.getFromConfig(FjordConfigData.class, Paths.get("config", "simplexterrain", "noisemodifiers", "fjords.json"));
	}

	@Override
	public double modify(int x, int z, double currentNoiseValue, double scaleValue) {
		double noise = noiseSampler.sample((double) x / config.scale, (double) z / config.scale);

		double dist = Math.abs(noise - config.threshold);

		if (currentNoiseValue > (SimplexTerrain.CONFIG.seaLevel + 3) - SimplexTerrain.CONFIG.baseHeight) // don't make the fjord cursed
		if (dist <= config.size) {
			currentNoiseValue = -60 + ((dist*config.depthModifier) + 30);
		}
		return currentNoiseValue;
	}
}
