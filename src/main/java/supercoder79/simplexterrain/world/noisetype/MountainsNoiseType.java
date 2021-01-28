package supercoder79.simplexterrain.world.noisetype;

import net.minecraft.util.math.MathHelper;
import net.minecraft.world.gen.ChunkRandom;
import supercoder79.simplexterrain.api.noise.OctaveNoiseSampler;
import supercoder79.simplexterrain.noise.gradient.OpenSimplexNoise;
import supercoder79.simplexterrain.world.BiomeData;

public class MountainsNoiseType implements NoiseType {
    private OctaveNoiseSampler<OpenSimplexNoise> noise;
    private OpenSimplexNoise ridged;

    @Override
    public void init(ChunkRandom random) {
        this.noise = new OctaveNoiseSampler<>(OpenSimplexNoise.class, random, 4, 160, 30, 16);
        this.ridged = new OpenSimplexNoise(random.nextLong());
    }

    @Override
    public double modify(int x, int z, double currentNoiseValue, double weight, BiomeData data) {
        double ridgedNoise = Math.abs(1 - this.ridged.sample(x / 60.0, z / 60.0)) * 12 * weight * weight;
        double addition = MathHelper.lerp(MathHelper.perlinFade(weight), 0, 10);
        double noise = this.noise.sample(x, z) * weight;

        return 3 + addition + noise + ridgedNoise;
    }
}
