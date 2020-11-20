package supercoder79.simplexterrain.world.noisemodifier;

import net.minecraft.util.math.MathHelper;
import supercoder79.simplexterrain.SimplexTerrain;
import supercoder79.simplexterrain.api.noise.Noise;
import supercoder79.simplexterrain.api.noisemodifier.NoiseModifier;
import supercoder79.simplexterrain.configs.ConfigHelper;
import supercoder79.simplexterrain.configs.noisemodifiers.MushroomFieldsConfigData;
import supercoder79.simplexterrain.noise.NoiseMath;
import supercoder79.simplexterrain.noise.gradient.OpenSimplexNoise;
import supercoder79.simplexterrain.world.BiomeData;

import java.nio.file.Paths;

public class MushroomIslandsNoiseModifier implements NoiseModifier {
    private Noise maskNoise;
    private MushroomFieldsConfigData config;

    @Override
    public void init(long seed) {
        this.maskNoise = new OpenSimplexNoise(seed - 146);
    }

    @Override
    public void setup() {
        config = ConfigHelper.getFromConfig(MushroomFieldsConfigData.class, Paths.get("config", "simplexterrain", "noisemodifiers", "mushroom_islands.json"));
    }

    @Override
    public double modify(int x, int z, double currentNoiseValue, BiomeData data) {
        double mNoise = maskNoise.sample(x / config.scale, z / config.scale);
        if (mNoise > config.threshold) {
            double ndelta = (mNoise - config.threshold) * 50;
            double add = MathHelper.clampedLerp(0, config.height, ndelta);
            double delta = (SimplexTerrain.CONFIG.seaLevel - 15) - NoiseMath.sigmoid(currentNoiseValue);

            if (delta > 0) {
                double end = MathHelper.clampedLerp(0, add, delta / 1.5);

                currentNoiseValue += end;
                data.setMushroomIsland();
            }
        }

        return currentNoiseValue;
    }
}
