package supercoder79.simplexterrain.world.noisetype;

import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkRandom;
import supercoder79.simplexterrain.world.BiomeData;

public interface NoiseType {
    void init(ChunkRandom random);

    double modify(int x, int z, double currentNoiseValue, double weight, BiomeData data);

    default RegistryKey<Biome> modify(int y, RegistryKey<Biome> existing) {
        return existing;
    }
}
