package supercoder79.simplexterrain.world;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.function.BiFunction;

import com.google.common.collect.Maps;
import com.mojang.datafixers.Dynamic;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.World;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.OverworldChunkGeneratorConfig;
import net.minecraft.world.level.LevelGeneratorOptions;
import net.minecraft.world.level.LevelGeneratorType;
import supercoder79.simplexterrain.SimplexTerrain;
import supercoder79.simplexterrain.world.gen.SimplexBiomeSource;
import supercoder79.simplexterrain.world.gen.SimplexBiomeSourceConfig;
import supercoder79.simplexterrain.world.gen.SimplexChunkGenerator;

public class WorldType<T extends ChunkGenerator<?>> {
	public static final Map<LevelGeneratorType, WorldType<?>> LGT_TO_WT_MAP = Maps.newHashMap();
	public static final Map<String, WorldType<?>> STR_TO_WT_MAP = Maps.newHashMap();

	public LevelGeneratorType generatorType;
	public final WorldTypeChunkGeneratorFactory<T> chunkGenSupplier;

	public WorldType(String name, WorldTypeChunkGeneratorFactory<T> chunkGenSupplier) {
		Constructor<LevelGeneratorType> constructor = null;
		try {
			constructor = LevelGeneratorType.class.getDeclaredConstructor(int.class, String.class, BiFunction.class);
			constructor.setAccessible(true);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

		try {
			this.generatorType = constructor.newInstance(FabricLoader.getInstance().isModLoaded("cwt") ? 10 : 9, name, (BiFunction<LevelGeneratorType, Dynamic<?>, LevelGeneratorOptions>) LevelGenUtil::makeChunkGenerator);
			generatorType.setCustomizable(false);
		} catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
			System.out.println("oh god oh frick");
			e.printStackTrace();
		}

		//There is nothing wrong that could happen with this
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
			if (SimplexTerrain.CONFIG.simplexIsDefault) {
				LevelGeneratorType.TYPES[FabricLoader.getInstance().isModLoaded("cwt") ? 10 : 9] = null;
				LevelGeneratorType[] temp = new LevelGeneratorType[16];
				for (int i = 0; i < LevelGeneratorType.TYPES.length; i++) {
					if (i == LevelGeneratorType.TYPES.length - 1) break;
					temp[i + 1] = LevelGeneratorType.TYPES[i];
				}
				temp[0] = generatorType;
				for (int i = 0; i < temp.length; i++) {
					LevelGeneratorType.TYPES[i] = temp[i];
				}
			}
		}

		this.chunkGenSupplier = chunkGenSupplier;

		if (this.generatorType == null) {
			throw new NullPointerException("An old world type has a null generator type: " + name + "!");
		}

		LGT_TO_WT_MAP.put(generatorType, this);
		STR_TO_WT_MAP.put(name, this);
	}

	public static final WorldType<SimplexChunkGenerator> SIMPLEX = new WorldType<>("simplex", (world) -> {
		OverworldChunkGeneratorConfig chunkGenConfig = new OverworldChunkGeneratorConfig();
		SimplexBiomeSourceConfig biomeSourceConfig = new SimplexBiomeSourceConfig(world.getLevelProperties()).setGeneratorSettings(chunkGenConfig);
		
		return SimplexTerrain.WORLDGEN_TYPE.create(world, new SimplexBiomeSource(biomeSourceConfig), chunkGenConfig);
	});

	public interface WorldTypeChunkGeneratorFactory<T extends ChunkGenerator<?>> {
		T create(World world);
	}
}
