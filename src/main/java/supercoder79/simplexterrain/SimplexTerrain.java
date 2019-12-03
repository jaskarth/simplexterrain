package supercoder79.simplexterrain;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.source.BiomeSourceType;
import net.minecraft.world.gen.chunk.OverworldChunkGeneratorConfig;
import supercoder79.simplexterrain.api.SimplexBiomes;
import supercoder79.simplexterrain.configs.Config;
import supercoder79.simplexterrain.configs.ConfigData;
import supercoder79.simplexterrain.terrain.biomesource.WorldBiomeSourceConfig;
import supercoder79.simplexterrain.terrain.biomesource.WorldBiomeSource;
import supercoder79.simplexterrain.terrain.WorldGeneratorType;
import supercoder79.simplexterrain.terrain.WorldType;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Function;

public class SimplexTerrain implements ModInitializer {

	public static WorldGeneratorType WORLDGEN_TYPE;
	public static BiomeSourceType<WorldBiomeSourceConfig, WorldBiomeSource> WORLD_BIOME_SOURCE;

	public static ConfigData CONFIG;

	static WorldType<?> loadMeOnClientPls; // make sure world types are loaded on client by referencing a field in onInitialize()
	@Override
	public void onInitialize() {
		CONFIG = Config.init();

		loadMeOnClientPls = WorldType.SIMPLEX;
		SimplexBiomes.init();

		//Reflection hacks
		Constructor<BiomeSourceType> constructor = null;
		try {
			constructor = BiomeSourceType.class.getDeclaredConstructor(Function.class, Function.class);
			constructor.setAccessible(true);
			WORLD_BIOME_SOURCE = constructor.newInstance((Function)WorldBiomeSource::new, (Function) WorldBiomeSourceConfig::new);
		} catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
			e.printStackTrace();
		}

		WORLDGEN_TYPE = Registry.register(Registry.CHUNK_GENERATOR_TYPE, new Identifier("simplexterrain", "simplex"), new WorldGeneratorType(false, OverworldChunkGeneratorConfig::new));
	}
}
