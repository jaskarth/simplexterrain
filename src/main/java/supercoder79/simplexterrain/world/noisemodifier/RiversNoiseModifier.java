package supercoder79.simplexterrain.world.noisemodifier;

import net.minecraft.util.math.MathHelper;
import supercoder79.simplexterrain.SimplexTerrain;
import supercoder79.simplexterrain.api.noise.Noise;
import supercoder79.simplexterrain.api.noise.OctaveNoiseSampler;
import supercoder79.simplexterrain.api.noisemodifier.NoiseModifier;
import supercoder79.simplexterrain.configs.ConfigHelper;
import supercoder79.simplexterrain.configs.noisemodifiers.RiversConfigData;
import supercoder79.simplexterrain.noise.NoiseMath;
import supercoder79.simplexterrain.noise.gradient.OpenSimplexNoise;
import supercoder79.simplexterrain.world.BiomeData;

import java.nio.file.Paths;
import java.util.Random;

public class RiversNoiseModifier implements NoiseModifier {
    public RiversConfigData config;
    private Noise noise;
    private Noise detailNoise;
    private Noise depthNoise;
    private Noise sizeNoise;

    @Override
    public void init(long seed) {
        noise = new OpenSimplexNoise(seed - 43);
        detailNoise = new OpenSimplexNoise(seed + 43);
        depthNoise = new OctaveNoiseSampler<>(OpenSimplexNoise.class, new Random(seed + 32), 3, config.scale / 2.0, 2.5, 6);
        sizeNoise = new OpenSimplexNoise(seed + 53);
    }

    @Override
    public void setup() {
        config = ConfigHelper.getFromConfig(RiversConfigData.class, Paths.get("config", "simplexterrain", "noisemodifiers", "rivers.json"));
    }

    @Override
    public double modify(int x, int z, double currentNoiseValue, BiomeData data) {
        double noise = this.noise.sample(x / config.scale, z / config.scale) + (detailNoise.sample(x / config.scale * 5.0, z / config.scale * 5.0) * 0.2);


        double depth = config.depth + depthNoise.sample(x, z);
        double size = config.size + (sizeNoise.sample(x / config.scale * 3.0, z / config.scale * 3.0) * 0.025);

        if (currentNoiseValue > depth) {
            if (noise < size && noise > -size) {
                // Interpolate downwards
                currentNoiseValue = MathHelper.lerp(smoothstep(noise / size), currentNoiseValue, depth);

                double realY = NoiseMath.sigmoid(currentNoiseValue);

                // Place a river if we're less than sea level, otherwise force place lowlands to prevent beach gen
                if (realY < SimplexTerrain.CONFIG.seaLevel) {
                    data.setRiver();
                } else if (realY <= SimplexTerrain.CONFIG.seaLevel + 9) {
                    data.setForcedLowlands();
                }
            }
        }

        return currentNoiseValue;
    }
    public static double smoothstep(double t) {
        return (1 - t*t)*(1 - t*t)*(1 - t*t);
    }
}
