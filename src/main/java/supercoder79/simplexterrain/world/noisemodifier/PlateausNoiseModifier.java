package supercoder79.simplexterrain.world.noisemodifier;

import net.minecraft.util.math.MathHelper;
import net.minecraft.world.gen.ChunkRandom;
import supercoder79.simplexterrain.SimplexTerrain;
import supercoder79.simplexterrain.api.noise.Noise;
import supercoder79.simplexterrain.api.noise.OctaveNoiseSampler;
import supercoder79.simplexterrain.api.noisemodifier.NoiseModifier;

public class PlateausNoiseModifier implements NoiseModifier {
    private OctaveNoiseSampler<? extends Noise> plateauNoise;

    @Override
    public void init(long seed) {
        plateauNoise = new OctaveNoiseSampler<>(SimplexTerrain.CONFIG.noiseGenerator.noiseClass, new ChunkRandom(seed + 3), 2, 256, 1, 1);
    }

    @Override
    public void setup() {

    }

    @Override
    public double modify(int x, int z, double currentNoiseValue) {
        double noise = plateauNoise.sample(x, z);

        if (noise > 0.4) {
            // interpolation reach = 0.045
            double fade =  0.445 - noise;
            // Create a smooth interpolation
            if (fade > 0) {
                fade = Math.abs(fade);
                fade /= 0.045;
                fade = smoothstep(1 - fade);
            } else {
                // This is to make the rest of the plateau flat-ish (outside of the interpolation region)
                fade = 1;
            }

            currentNoiseValue += MathHelper.lerp(fade, 0, 12);
        }

        return currentNoiseValue;
    }

    private static double smoothstep(double d) {
        return d * d * d * (d * (d * 6.0D - 15.0D) + 10.0D);
    }
}
