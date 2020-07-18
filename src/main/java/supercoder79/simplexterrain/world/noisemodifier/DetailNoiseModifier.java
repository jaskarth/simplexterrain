package supercoder79.simplexterrain.world.noisemodifier;

import net.minecraft.world.gen.ChunkRandom;
import supercoder79.simplexterrain.SimplexTerrain;
import supercoder79.simplexterrain.api.noise.Noise;
import supercoder79.simplexterrain.api.noise.OctaveNoiseSampler;
import supercoder79.simplexterrain.api.noisemodifier.NoiseModifier;
import supercoder79.simplexterrain.configs.ConfigUtil;
import supercoder79.simplexterrain.configs.noisemodifiers.DetailsConfigData;

import java.nio.file.Paths;

public class DetailNoiseModifier implements NoiseModifier {
    private DetailsConfigData config;
    private OctaveNoiseSampler<? extends Noise> detailNoise;

    @Override
    public void init(long seed) {
        detailNoise = new OctaveNoiseSampler<>(SimplexTerrain.CONFIG.noiseGenerator.noiseClass, new ChunkRandom(seed), config.octaves, config.frequency, config.amplitudeHigh, config.amplitudeLow);
    }

    @Override
    public void setup() {
        config = ConfigUtil.getFromConfig(DetailsConfigData.class, Paths.get("config", "simplexterrain", "noisemodifiers", "details.json"));
    }

    @Override
    public double modify(int x, int z, double currentNoiseValue) {
        return currentNoiseValue + detailNoise.sample(x, z);
    }

    @Override
    public String getName() {
    	return "DETAIL";
    }
}
