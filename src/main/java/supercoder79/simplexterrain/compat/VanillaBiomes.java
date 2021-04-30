package supercoder79.simplexterrain.compat;

import com.google.common.collect.ImmutableList;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.ChunkRandom;
import supercoder79.simplexterrain.world.noisetype.NoiseTypeCache;
import supercoder79.simplexterrain.world.noisetype.NoiseTypeHolder;
import supercoder79.simplexterrain.world.noisetype.NoiseTypePicker;
import supercoder79.simplexterrain.world.noisetype.forest.HillsNoiseType;
import supercoder79.simplexterrain.world.noisetype.plains.LowLyingPlainsNoiseType;
import supercoder79.simplexterrain.world.noisetype.plains.ForestedHillsNoiseType;
import supercoder79.simplexterrain.world.noisetype.plains.MountainsNoiseType;
import supercoder79.simplexterrain.world.noisetype.plains.PlainsNoiseType;

import java.util.function.Function;

public class VanillaBiomes {
    public static void register() {
        register(BiomeKeys.PLAINS, VanillaBiomes::createPlains);
        register(BiomeKeys.FOREST, VanillaBiomes::createForest);
    }

    private static void register(RegistryKey<Biome> key, Function<ChunkRandom, NoiseTypeCache> factory) {
        NoiseTypeHolder.BIOME_FACTORIES.put(key, factory);
    }

    private static NoiseTypeCache createPlains(ChunkRandom random) {
        PlainsNoiseType plains = new PlainsNoiseType();
        plains.init(random);

        ForestedHillsNoiseType hills = new ForestedHillsNoiseType();
        hills.init(random);

        MountainsNoiseType mountains = new MountainsNoiseType();
        mountains.init(random);

        LowLyingPlainsNoiseType lakes = new LowLyingPlainsNoiseType();
        lakes.init(random);

        NoiseTypePicker picker = new NoiseTypePicker(random, ImmutableList.of(plains, hills, lakes, mountains));
        return new NoiseTypeCache(picker);
    }

    private static NoiseTypeCache createForest(ChunkRandom random) {
        PlainsNoiseType plains = new PlainsNoiseType();
        plains.init(random);

        HillsNoiseType hills = new HillsNoiseType();
        hills.init(random);

        NoiseTypePicker picker = new NoiseTypePicker(random, ImmutableList.of(plains, hills));
        return new NoiseTypeCache(picker);
    }
}
