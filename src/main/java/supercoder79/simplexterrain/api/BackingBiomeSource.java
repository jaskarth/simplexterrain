package supercoder79.simplexterrain.api;

import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public interface BackingBiomeSource {
    RegistryKey<Biome> getBacking(int x, int z);
}
