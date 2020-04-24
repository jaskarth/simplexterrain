package supercoder79.simplexterrain.world;

import com.mojang.datafixers.Dynamic;
import net.minecraft.world.gen.chunk.OverworldChunkGeneratorConfig;
import net.minecraft.world.level.LevelGeneratorOptions;
import net.minecraft.world.level.LevelGeneratorType;
import supercoder79.simplexterrain.world.gen.SimplexBiomeSource;
import supercoder79.simplexterrain.world.gen.SimplexBiomeSourceConfig;
import supercoder79.simplexterrain.world.gen.SimplexChunkGenerator;

public class LevelGenUtil {

    //why is this even necesarry????
    public static LevelGeneratorOptions makeChunkGenerator(LevelGeneratorType levelGeneratorType, Dynamic<?> dynamic) {
        OverworldChunkGeneratorConfig chunkGeneratorConfig = new OverworldChunkGeneratorConfig();
        return new LevelGeneratorOptions(levelGeneratorType, dynamic, (world) -> new SimplexChunkGenerator(world, new SimplexBiomeSource(new SimplexBiomeSourceConfig(world.getLevelProperties())), chunkGeneratorConfig));
    }
}
