package supercoder79.simplexterrain.world.noisetype.desert;

import net.minecraft.world.gen.ChunkRandom;
import supercoder79.simplexterrain.api.noise.OctaveNoiseSampler;
import supercoder79.simplexterrain.noise.gradient.OpenSimplexNoise;
import supercoder79.simplexterrain.world.BiomeData;
import supercoder79.simplexterrain.world.noisetype.NoiseType;

public class DunesNoiseType implements NoiseType {
    private OpenSimplexNoise ridged;
    private OctaveNoiseSampler<OpenSimplexNoise> noise;

    @Override
    public void init(ChunkRandom random) {
        this.ridged = new OpenSimplexNoise(random.nextLong());
        this.noise = new OctaveNoiseSampler<>(OpenSimplexNoise.class, random, 2, 300, 20, 10);
    }

    @Override
    public double modify(int x, int z, double currentNoiseValue, double weight, BiomeData data) {
        double ridgedNoise = (1 - Math.abs(this.ridged.sample(x / 120.0, z / 120.0))) * 20 * weight;

        return ridgedNoise + this.noise.sample(x, z) * weight;
    }
}
