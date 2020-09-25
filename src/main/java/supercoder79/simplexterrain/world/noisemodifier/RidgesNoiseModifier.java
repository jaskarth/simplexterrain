package supercoder79.simplexterrain.world.noisemodifier;

import net.minecraft.world.gen.ChunkRandom;
import supercoder79.simplexterrain.SimplexTerrain;
import supercoder79.simplexterrain.api.noise.Noise;
import supercoder79.simplexterrain.api.noise.OctaveNoiseSampler;
import supercoder79.simplexterrain.api.noisemodifier.NoiseModifier;
import supercoder79.simplexterrain.configs.ConfigHelper;
import supercoder79.simplexterrain.configs.noisemodifiers.RidgesConfigData;

import java.nio.file.Paths;

public class RidgesNoiseModifier implements NoiseModifier {
    private RidgesConfigData config;
    private OctaveNoiseSampler<? extends Noise> ridgedNoise;

    @Override
    public void init(long seed) {
        ridgedNoise = new OctaveNoiseSampler<>(SimplexTerrain.CONFIG.noiseGenerator.noiseClass, new ChunkRandom(seed - 20), config.octaves, config.frequency, 1, 1);
    }

    @Override
    public void setup() {
        config = ConfigHelper.getFromConfig(RidgesConfigData.class, Paths.get("config", "simplexterrain", "noisemodifiers", "ridges.json"));
    }

    @Override
    public double modify(int x, int z, double currentNoiseValue) {
        return currentNoiseValue + ((1 - Math.abs(ridgedNoise.sample(x, z))) * config.amplitude);
    }
}
