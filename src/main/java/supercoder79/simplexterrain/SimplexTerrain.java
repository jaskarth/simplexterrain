package supercoder79.simplexterrain;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.source.BiomeSourceType;
import net.minecraft.world.biome.source.VanillaLayeredBiomeSourceConfig;
import net.minecraft.world.gen.chunk.OverworldChunkGeneratorConfig;
import supercoder79.simplexterrain.terrain.WorldBiomeSource;
import supercoder79.simplexterrain.terrain.WorldGeneratorType;
import supercoder79.simplexterrain.terrain.WorldType;

public class SimplexTerrain implements ModInitializer {

	public static WorldGeneratorType WORLDGEN_TYPE;
	public static BiomeSourceType<VanillaLayeredBiomeSourceConfig, WorldBiomeSource> WORLD_BIOME_SOURCE;

	static WorldType<?> loadMeOnClientPls; // make sure world types are loaded on client by referencing a field in onInitialize()
	@Override
	public void onInitialize() {
		loadMeOnClientPls = WorldType.SIMPLEX;

		WORLD_BIOME_SOURCE = new BiomeSourceType(WorldBiomeSource::new, VanillaLayeredBiomeSourceConfig::new);

		WORLDGEN_TYPE = Registry.register(Registry.CHUNK_GENERATOR_TYPE, new Identifier("simplexterrain", "simplex"), new WorldGeneratorType(false, OverworldChunkGeneratorConfig::new));
	}
}
