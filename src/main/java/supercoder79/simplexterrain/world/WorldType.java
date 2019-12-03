package supercoder79.simplexterrain.world;

import com.google.common.collect.Maps;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.World;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.OverworldChunkGeneratorConfig;
import net.minecraft.world.level.LevelGeneratorType;
import supercoder79.simplexterrain.SimplexTerrain;
import supercoder79.simplexterrain.mixin.AccessorLevelGeneratorType;
import supercoder79.simplexterrain.world.gen.WorldBiomeSource;
import supercoder79.simplexterrain.world.gen.WorldBiomeSourceConfig;
import supercoder79.simplexterrain.world.gen.WorldChunkGenerator;

import java.util.Map;

public class WorldType<T extends ChunkGenerator<?>> {
	public static final Map<LevelGeneratorType, WorldType<?>> LGT_TO_WT_MAP = Maps.newHashMap();
	public static final Map<String, WorldType<?>> STR_TO_WT_MAP = Maps.newHashMap();

	public final LevelGeneratorType generatorType;
	public final WorldTypeChunkGeneratorFactory<T> chunkGenSupplier;

	public WorldType(String name, WorldTypeChunkGeneratorFactory<T> chunkGenSupplier) {
		this.generatorType = AccessorLevelGeneratorType.create(FabricLoader.getInstance().isModLoaded("cwt") ? 10 : 9, name);
		generatorType.setCustomizable(false);
		this.chunkGenSupplier = chunkGenSupplier;

		if (this.generatorType == null) {
			throw new NullPointerException("An old world type has a null generator type: " + name + "!");
		}

		LGT_TO_WT_MAP.put(generatorType, this);
		STR_TO_WT_MAP.put(name, this);
	}

	public static final WorldType<WorldChunkGenerator> SIMPLEX = new WorldType<>("simplex", (world) -> {
		OverworldChunkGeneratorConfig chunkGenConfig = new OverworldChunkGeneratorConfig();
		WorldBiomeSourceConfig biomeSourceConfig = new WorldBiomeSourceConfig(world.getLevelProperties()).setGeneratorSettings(chunkGenConfig);
		
		return SimplexTerrain.WORLDGEN_TYPE.create(world, new WorldBiomeSource(biomeSourceConfig), chunkGenConfig);
	});

	public interface WorldTypeChunkGeneratorFactory<T extends ChunkGenerator<?>> {
		T create(World world);
	}
}
