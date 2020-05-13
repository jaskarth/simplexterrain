package supercoder79.simplexterrain.world.noisemodifier;

import net.minecraft.world.gen.ChunkRandom;
import supercoder79.simplexterrain.SimplexTerrain;
import supercoder79.simplexterrain.api.noise.Noise;
import supercoder79.simplexterrain.api.noise.OctaveNoiseSampler;
import supercoder79.simplexterrain.api.noisemodifier.NoiseModifier;

public class RidgesNoiseModifier implements NoiseModifier {
    private OctaveNoiseSampler<? extends Noise> ridgedNoise;

    @Override
    public void init(long seed) {
        ridgedNoise = new OctaveNoiseSampler<>(SimplexTerrain.CONFIG.noiseGenerator.noiseClass, new ChunkRandom(seed - 20), 3, 768, 1, 1);
    }

    @Override
    public void setup() {

    }

    @Override
    public double modify(int x, int z, double currentNoiseValue) {
        return currentNoiseValue + ((1 - Math.abs(ridgedNoise.sample(x, z))) * 80);
    }
}
