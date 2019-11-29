package supercoder79.simplexterrain.terrain;

import net.minecraft.world.World;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.chunk.ChunkGeneratorType;
import net.minecraft.world.gen.chunk.OverworldChunkGeneratorConfig;

import java.util.function.Supplier;

public class WorldGeneratorType extends ChunkGeneratorType<OverworldChunkGeneratorConfig, WorldChunkGenerator> {

    public WorldGeneratorType(boolean buffetScreen, Supplier<OverworldChunkGeneratorConfig> configSupplier) {
        super(null, buffetScreen, configSupplier);
    }

    public static void init() {
        // NO-OP
    }

    @Override
    public WorldChunkGenerator create(World world, BiomeSource biomeSource, OverworldChunkGeneratorConfig config) {
        return new WorldChunkGenerator(world, biomeSource, config);
    }
}