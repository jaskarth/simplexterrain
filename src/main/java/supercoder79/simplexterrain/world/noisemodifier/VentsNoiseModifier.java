package supercoder79.simplexterrain.world.noisemodifier;

import java.nio.file.Paths;

import supercoder79.simplexterrain.api.noise.NoiseModifier;
import supercoder79.simplexterrain.configs.ConfigUtil;
import supercoder79.simplexterrain.configs.noisemodifiers.VentsConfigData;
import supercoder79.simplexterrain.noise.gradient.OpenSimplexNoise;

public class VentsNoiseModifier extends NoiseModifier {
	private VentsConfigData config;
	private OpenSimplexNoise noiseSampler;

	public VentsNoiseModifier() {
		super(4L);
	}

	@Override
	public void init(long seed) {
		noiseSampler = new OpenSimplexNoise(seed - 56);
	}

	@Override
	public void setup() {
		config = ConfigUtil.getFromConfig(VentsConfigData.class, Paths.get("config", "simplexterrain", "noisemodifiers", "vents.json"));
	}

	@Override
	public double modify(int x, int z, double currentNoiseValue, double scaleValue) {
		double noise = noiseSampler.sample((double) x / config.scale, (double) z / config.scale);

		double dist = Math.abs(noise - config.threshold);

		if (dist <= config.size) {
			currentNoiseValue += ((dist*config.heightModifier) + config.baseHeight);
		} else if (noise > config.threshold) {
			currentNoiseValue -= noise*config.depthCoefficient;
		}
		return currentNoiseValue;
	}
}
