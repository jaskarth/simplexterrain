package supercoder79.simplexterrain.world.noisemodifier;

import net.minecraft.util.math.MathHelper;
import supercoder79.simplexterrain.SimplexTerrain;
import supercoder79.simplexterrain.api.noise.Noise;
import supercoder79.simplexterrain.api.noisemodifier.NoiseModifier;
import supercoder79.simplexterrain.configs.ConfigHelper;
import supercoder79.simplexterrain.configs.noisemodifiers.RiversConfigData;
import supercoder79.simplexterrain.noise.NoiseMath;
import supercoder79.simplexterrain.noise.gradient.OpenSimplexNoise;

import java.nio.file.Paths;

public class RiversNoiseModifier implements NoiseModifier {
    public RiversConfigData config;
    private Noise noise;
    private Noise detailNoise;

    @Override
    public void init(long seed) {
        noise = new OpenSimplexNoise(seed - 43);
        detailNoise = new OpenSimplexNoise(seed + 43);
    }

    @Override
    public void setup() {
        config = ConfigHelper.getFromConfig(RiversConfigData.class, Paths.get("config", "simplexterrain", "noisemodifiers", "rivers.json"));
    }

    @Override
    public double modify(int x, int z, double currentNoiseValue) {
        double noise = this.noise.sample(x / config.scale, z / config.scale) + (detailNoise.sample(x / config.scale * 5.0, z / config.scale * 5.0) * 0.05);

        double dist = Math.abs(noise - 0.13);

        //TODO: reverse sigmoid
        if (NoiseMath.sigmoid(currentNoiseValue) > SimplexTerrain.CONFIG.seaLevel - 1) { // TODO: this creates a small crease where it meets the ocean
            if (dist <= config.size) {
                currentNoiseValue = MathHelper.lerp(smoothstep(dist / config.size), config.depth, currentNoiseValue);
            }
        }

        return currentNoiseValue;
    }

    public static double smoothstep(double d) {
        return d * d * d * (d * (d * 6.0 - 15.0) + 10.0);
    }
}
