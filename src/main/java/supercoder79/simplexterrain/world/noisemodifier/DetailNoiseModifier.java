package supercoder79.simplexterrain.world.noisemodifier;

import net.minecraft.world.gen.ChunkRandom;
import supercoder79.simplexterrain.SimplexTerrain;
import supercoder79.simplexterrain.api.noise.Noise;
import supercoder79.simplexterrain.api.noise.OctaveNoiseSampler;
import supercoder79.simplexterrain.api.noisemodifier.NoiseModifier;

public class DetailNoiseModifier implements NoiseModifier {
    private OctaveNoiseSampler<? extends Noise> detailNoise;

    @Override
    public void init(long seed) {
        detailNoise = new OctaveNoiseSampler<>(SimplexTerrain.CONFIG.noiseGenerator.noiseClass, new ChunkRandom(seed), 2, 32, 2, 2);
    }

    @Override
    public void setup() {

    }

    @Override
    public double modify(int x, int z, double currentNoiseValue) {
        return currentNoiseValue + detailNoise.sample(x, z);
    }
}
