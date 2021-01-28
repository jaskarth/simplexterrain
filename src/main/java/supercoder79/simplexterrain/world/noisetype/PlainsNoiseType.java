package supercoder79.simplexterrain.world.noisetype;

import net.minecraft.world.gen.ChunkRandom;
import supercoder79.simplexterrain.SimplexTerrain;
import supercoder79.simplexterrain.api.noise.Noise;
import supercoder79.simplexterrain.api.noise.OctaveNoiseSampler;
import supercoder79.simplexterrain.noise.gradient.OpenSimplexNoise;
import supercoder79.simplexterrain.world.BiomeData;

public class PlainsNoiseType implements NoiseType {
    private OctaveNoiseSampler<OpenSimplexNoise> noise;
    @Override
    public void init(ChunkRandom random) {
        this.noise = new OctaveNoiseSampler<>(OpenSimplexNoise.class, random, 4, 160, 8, 8);
    }

    @Override
    public double modify(int x, int z, double currentNoiseValue, double weight, BiomeData data) {
        return 3 + this.noise.sample(x, z);
    }
}
