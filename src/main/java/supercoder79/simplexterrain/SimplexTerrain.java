package supercoder79.simplexterrain;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.chunk.OverworldChunkGeneratorConfig;
import supercoder79.simplexterrain.api.SimplexBiomes;
import supercoder79.simplexterrain.api.SimplexClimate;
import supercoder79.simplexterrain.configs.Config;
import supercoder79.simplexterrain.configs.ConfigData;
import supercoder79.simplexterrain.world.WorldType;
import supercoder79.simplexterrain.world.gen.WorldGeneratorType;

public class SimplexTerrain implements ModInitializer {
	public static WorldGeneratorType WORLDGEN_TYPE;

	public static ConfigData CONFIG;

	static WorldType<?> loadMeOnClientPls; // make sure world types are loaded on client by referencing a field in onInitialize()
	
	private static Identifier SWAMP;
	private static Identifier PLAINS;
	private static Identifier FOREST;
	private static Identifier TAIGA;

	@Override
	public void onInitialize() {
		SWAMP = biomeId(Biomes.SWAMP);
		PLAINS = biomeId(Biomes.PLAINS);
		FOREST = biomeId(Biomes.FOREST);
		TAIGA = biomeId(Biomes.TAIGA);
		
		CONFIG = Config.init();

		loadMeOnClientPls = WorldType.SIMPLEX;
		addDefaultBiomes();

		WORLDGEN_TYPE = Registry.register(Registry.CHUNK_GENERATOR_TYPE, new Identifier("simplexterrain", "simplex"), new WorldGeneratorType(false, OverworldChunkGeneratorConfig::new));
	}
	
	private static void addDefaultBiomes() {
		//holy shit this code is still cursed

		//midlands
		double birchForestWeight = 0.8;
		double darkForestWeight = 1.0;
		double forestWeight = 1.0;

		//highlands
		double mountainEdgeWeight = 0.8;
		double woodedMountainsWeight = 0.8;
		double taigaWeight = 1.0;

		//toplands
		double gravellyMountainsWeight = 0.6;
		double woodedMountainsToplandsWeight = 0.7;
		double iceSpikesWeight = 1.6;
		double mountainsWeight = 1.0;

		if (SimplexTerrain.CONFIG.doModCompat) {
			//mod compat
			if (FabricLoader.getInstance().isModLoaded("winterbiomemod")) {
				addToHighlands(new Identifier("winterbiomemod", "white_oaks"), 8);
				addToHighlands(new Identifier("winterbiomemod", "white_oaks_thicket"), 10);
				addToHighlands(new Identifier("winterbiomemod", "alpine"), 7);
				addToHighlands(new Identifier("winterbiomemod", "subalpine"), 10);
				addToHighlands(new Identifier("winterbiomemod", "subalpine_crag"), 10);

				addToToplands(new Identifier("winterbiomemod", "alpine_peaks"), 8);
				addToToplands(new Identifier("winterbiomemod", "alpine_glacier"), 9);

				System.out.println("Winter biomes registered!");
			}

			if (FabricLoader.getInstance().isModLoaded("traverse")) {
				addToLowlands(new Identifier("traverse", "arid_highlands"), 10);
				addToHighlands(new Identifier("traverse", "cliffs"), 12);
				addToMidlands(new Identifier("traverse", "coniferous_forest"), 11);

				addToHighlands(new Identifier("traverse", "snowy_coniferous_forest"), 9);

				addToLowlands(new Identifier("traverse", "desert_shrubland"), 12);
				addToLowlands(new Identifier("traverse", "lush_swamp"), 11);
				addToLowlands(new Identifier("traverse", "meadow"), 9);

				addToLowlands(new Identifier("traverse", "mini_jungle"), 10);
				addToMidlands(new Identifier("traverse", "meadow"), 9);

				addToMidlands(new Identifier("traverse", "plains_plateau"), 11);
				addToLowlands(new Identifier("traverse", "plains_plateau"), 9);

				addToMidlands(new Identifier("traverse", "wooded_plateau"), 9);
				addToLowlands(new Identifier("traverse", "wooded_plateau"), 11);

				addToMidlands(new Identifier("traverse", "rolling_hills"), 8);
				addToLowlands(new Identifier("traverse", "rolling_hills"), 9);

				addToMidlands(new Identifier("traverse", "woodlands"), 10);
				addToLowlands(new Identifier("traverse", "woodlands"), 12);


				System.out.println("Traverse biomes registered!");
			}

			if (FabricLoader.getInstance().isModLoaded("terrestria")) {
				System.out.println("Terrestria biomes registered!");
			}
		}

		addVanillaLowlands();

		addToMidlands(Registry.BIOME.getId(Biomes.BIRCH_FOREST), birchForestWeight);
		addToMidlands(Registry.BIOME.getId(Biomes.DARK_FOREST), darkForestWeight);

		addToHighlands(Registry.BIOME.getId(Biomes.MOUNTAIN_EDGE), mountainEdgeWeight);
		addToHighlands(Registry.BIOME.getId(Biomes.WOODED_MOUNTAINS), woodedMountainsWeight);

		addToToplands(Registry.BIOME.getId(Biomes.GRAVELLY_MOUNTAINS), gravellyMountainsWeight);
		addToToplands(Registry.BIOME.getId(Biomes.WOODED_MOUNTAINS), woodedMountainsToplandsWeight);
		addToToplands(Registry.BIOME.getId(Biomes.ICE_SPIKES), iceSpikesWeight);
	}
	
	private static void addVanillaLowlands() {
		final double swampWeight = 1.3;
		final double desertWeight = 1.3;
		final double jungleWeight = 1.5;
		final double savannahWeight = 1.2;
		final double plainsWeight = 1.0;
		final double forestWeight = 1.0;
		final double taigaWeight = 1.2;
		final double snowyTaigaWeight = 0.8;
		final double snowyTundraWeight = 2.2;
		
		SimplexBiomes.addLowlandsBiome(biomeId(Biomes.DESERT), SimplexClimate.DRY_TROPICAL, desertWeight);
		
		SimplexBiomes.addLowlandsBiome(biomeId(Biomes.SAVANNA), SimplexClimate.TROPICAL, savannahWeight);
		SimplexBiomes.addLowlandsBiome(PLAINS, SimplexClimate.TROPICAL, plainsWeight);
		
		SimplexBiomes.addLowlandsBiome(biomeId(Biomes.JUNGLE), SimplexClimate.LUSH_TROPICAL, jungleWeight);
		SimplexBiomes.addLowlandsBiome(SWAMP, SimplexClimate.LUSH_TROPICAL, swampWeight / 2);
		
		SimplexBiomes.addLowlandsBiome(PLAINS, SimplexClimate.DRY_TEMPERATE, plainsWeight);
		
		SimplexBiomes.addLowlandsBiome(SWAMP, SimplexClimate.TEMPERATE, swampWeight);
		SimplexBiomes.addLowlandsBiome(PLAINS, SimplexClimate.TEMPERATE, plainsWeight);
		SimplexBiomes.addLowlandsBiome(FOREST, SimplexClimate.TEMPERATE, forestWeight * 0.8);
		
		SimplexBiomes.addLowlandsBiome(SWAMP, SimplexClimate.LUSH_TEMPERATE, swampWeight);
		SimplexBiomes.addLowlandsBiome(PLAINS, SimplexClimate.LUSH_TEMPERATE, plainsWeight);
		SimplexBiomes.addLowlandsBiome(FOREST, SimplexClimate.LUSH_TEMPERATE, forestWeight * 1.5);
		
		SimplexBiomes.addLowlandsBiome(PLAINS, SimplexClimate.DRY_BOREAL, plainsWeight);
		SimplexBiomes.addLowlandsBiome(TAIGA, SimplexClimate.DRY_BOREAL, taigaWeight);
		
		SimplexBiomes.addLowlandsBiome(TAIGA, SimplexClimate.BOREAL, taigaWeight);
		
		SimplexBiomes.addLowlandsBiome(TAIGA, SimplexClimate.LUSH_BOREAL, taigaWeight);
		
		SimplexBiomes.addLowlandsBiome(biomeId(Biomes.SNOWY_TAIGA), SimplexClimate.SNOWY, snowyTaigaWeight);
		SimplexBiomes.addLowlandsBiome(biomeId(Biomes.SNOWY_TAIGA), SimplexClimate.SNOWY, snowyTundraWeight);
	}

	private static final Identifier biomeId(Biome biome) {
		return Registry.BIOME.getId(biome);
	}
}
