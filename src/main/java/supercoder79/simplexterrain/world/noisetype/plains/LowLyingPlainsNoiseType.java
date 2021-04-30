package supercoder79.simplexterrain.world.noisetype.plains;

import net.minecraft.util.math.Vec3d;
import net.minecraft.world.gen.ChunkRandom;
import supercoder79.simplexterrain.api.noise.OctaveNoiseSampler;
import supercoder79.simplexterrain.noise.gradient.OpenSimplexNoise;
import supercoder79.simplexterrain.world.BiomeData;
import supercoder79.simplexterrain.world.noisetype.NoiseType;

import java.util.Map;

public class LowLyingPlainsNoiseType implements NoiseType {
    private OctaveNoiseSampler<OpenSimplexNoise> noise;

    @Override
    public void init(ChunkRandom random) {
        this.noise = new OctaveNoiseSampler<>(OpenSimplexNoise.class, random, 4, 100, 8, 4);
    }

    @Override
    public double modify(int x, int z, double currentNoiseValue, double weight, BiomeData data) {
        return -1 + this.noise.sample(x, z);
    }

    @Override
    public void addToPicker(Map<Vec3d, NoiseType> points, double theta) {
        points.put(new Vec3d(Math.cos(theta) * Math.sqrt(3), 0, Math.sin(theta) * Math.sqrt(3)), this);
    }
}
