package supercoder79.simplexterrain.world.noisemodifier;

import net.minecraft.util.math.MathHelper;
import net.minecraft.world.gen.ChunkRandom;
import supercoder79.simplexterrain.SimplexTerrain;
import supercoder79.simplexterrain.api.noise.Noise;
import supercoder79.simplexterrain.api.noise.OctaveNoiseSampler;
import supercoder79.simplexterrain.api.noisemodifier.NoiseModifier;
import supercoder79.simplexterrain.configs.ConfigHelper;
import supercoder79.simplexterrain.configs.noisemodifiers.PlateausConfigData;
import supercoder79.simplexterrain.world.BiomeData;

import java.nio.file.Paths;

public class PlateausNoiseModifier implements NoiseModifier {
    private PlateausConfigData config;
    private OctaveNoiseSampler<? extends Noise> plateauNoise;

    @Override
    public void init(long seed) {
        plateauNoise = new OctaveNoiseSampler<>(SimplexTerrain.CONFIG.noiseGenerator.noiseClass, new ChunkRandom(seed + 3), config.octaves, config.frequency, 1, 1);
    }

    @Override
    public void setup() {
        config = ConfigHelper.getFromConfig(PlateausConfigData.class, Paths.get("config", "simplexterrain", "noisemodifiers", "plateaus.json"));
    }

    @Override
    public double modify(int x, int z, double currentNoiseValue, BiomeData data) {
        double noise = plateauNoise.sample(x, z);

        if (noise > config.threshold) {
            currentNoiseValue += MathHelper.lerp(fade(noise, config.threshold, config.interpolation), 0, config.height);
        }

        return currentNoiseValue;
    }

    private static double fade(double noise, double threshold, double interpolation) {
        double fade =  (threshold + interpolation) - noise;
        // Create a smooth interpolation
        if (fade > 0) {
            fade = Math.abs(fade);
            fade /= interpolation;
            fade = smoothstep(1 - fade);
        } else {
            // This is to make the rest of the plateau flat-ish (outside of the interpolation region)
            fade = 1;
        }

        return fade;
    }

    private static double smoothstep(double d) {
        return d * d * (3 - 2 * d);
    }
}
