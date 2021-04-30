package supercoder79.simplexterrain.world.noisetype;

import it.unimi.dsi.fastutil.ints.Int2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkRandom;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public class NoiseTypeHolder {
    public static final Map<RegistryKey<Biome>, Function<ChunkRandom, NoiseTypeCache>> BIOME_FACTORIES = new LinkedHashMap<>();
    public static final Map<NoiseType, Integer> IDS = new Reference2IntOpenHashMap<>();
    public static final Map<Integer, NoiseType> IDS_REVERSE = new Int2ReferenceOpenHashMap<>();
    public static int currentId = 0; // TODO: remove ugly shared state

    private static final Map<RegistryKey<Biome>, NoiseTypeCache> FOR_BIOME = new LinkedHashMap<>();

    public static void initialize(ChunkRandom random) {
        FOR_BIOME.clear();
        IDS.clear();
        currentId = 0;

        for (Map.Entry<RegistryKey<Biome>, Function<ChunkRandom, NoiseTypeCache>> entry : BIOME_FACTORIES.entrySet()) {
            FOR_BIOME.put(entry.getKey(), entry.getValue().apply(random));
        }
    }

    public static NoiseTypeCache get(RegistryKey<Biome> biome) {
        return FOR_BIOME.get(biome);
    }
}
