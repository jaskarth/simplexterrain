package supercoder79.simplexterrain.world.noisetype;

import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkRandom;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class NoiseTypeHolder {
    public static final Map<RegistryKey<Biome>, Function<ChunkRandom, NoiseTypeCache>> BIOME_FACTORIES = new HashMap<>();

    private static final Map<RegistryKey<Biome>, NoiseTypeCache> FOR_BIOME = new HashMap<>();

    public static void initialize(ChunkRandom random) {
        FOR_BIOME.clear();

        for (Map.Entry<RegistryKey<Biome>, Function<ChunkRandom, NoiseTypeCache>> entry : BIOME_FACTORIES.entrySet()) {
            FOR_BIOME.put(entry.getKey(), entry.getValue().apply(random));
        }
    }

    public static NoiseTypeCache get(RegistryKey<Biome> biome) {
        return FOR_BIOME.get(biome);
    }
}
