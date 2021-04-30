package supercoder79.simplexterrain.world.noisetype;

import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkRandom;
import supercoder79.simplexterrain.world.BiomeData;

import java.util.Map;

public interface NoiseType {
    void init(ChunkRandom random);

    double modify(int x, int z, double currentNoiseValue, double weight, BiomeData data);

    default RegistryKey<Biome> modifyBiome(int y, RegistryKey<Biome> existing) {
        return existing;
    }

    default void addToPicker(Map<Vec3d, NoiseType> points, double theta) {
        points.put(new Vec3d(Math.cos(theta) * Math.sqrt(2), 0, Math.sin(theta) * Math.sqrt(2)), this);
    }
}
