package supercoder79.simplexterrain.world.noisemodifier;

import net.minecraft.world.gen.ChunkRandom;
import supercoder79.simplexterrain.SimplexTerrain;
import supercoder79.simplexterrain.api.noise.Noise;
import supercoder79.simplexterrain.api.noise.OctaveNoiseSampler;
import supercoder79.simplexterrain.api.noisemodifier.NoiseModifier;

public class MountainsNoiseModifier implements NoiseModifier {
    private OctaveNoiseSampler<? extends Noise> mountainNoise;

    @Override
    public void init(long seed) {
        mountainNoise = new OctaveNoiseSampler<>(SimplexTerrain.CONFIG.noiseGenerator.noiseClass, new ChunkRandom(seed + 20), 3, 2800, 256, 64);
    }

    @Override
    public void setup() {

    }

    @Override
    public double modify(int x, int z, double currentNoiseValue) {
        return currentNoiseValue + Math.max(mountainNoise.sample(x, z), 0);
    }
}
