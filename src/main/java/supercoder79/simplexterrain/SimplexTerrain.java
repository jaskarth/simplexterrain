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
	private static Identifier BIRCH_FOREST;
	private static Identifier JUNGLE;
	private static Identifier MOUNTAINS;
	private static Identifier MOUNTAIN_EDGE;
	private static Identifier WOODED_MOUNTAINS;
	private static Identifier GRAVELLY_MOUNTAINS;

	@Override
	public void onInitialize() {
		SWAMP = biomeId(Biomes.SWAMP);
		PLAINS = biomeId(Biomes.PLAINS);
		FOREST = biomeId(Biomes.FOREST);
		TAIGA = biomeId(Biomes.TAIGA);
		BIRCH_FOREST = biomeId(Biomes.BIRCH_FOREST);
		JUNGLE = biomeId(Biomes.JUNGLE);
		MOUNTAINS = biomeId(Biomes.MOUNTAINS);
		MOUNTAIN_EDGE = biomeId(Biomes.MOUNTAIN_EDGE);
		WOODED_MOUNTAINS = biomeId(Biomes.WOODED_MOUNTAINS);
		GRAVELLY_MOUNTAINS = biomeId(Biomes.GRAVELLY_MOUNTAINS);

		CONFIG = Config.init();

		loadMeOnClientPls = WorldType.SIMPLEX;
		addDefaultBiomes();

		WORLDGEN_TYPE = Registry.register(Registry.CHUNK_GENERATOR_TYPE, new Identifier("simplexterrain", "simplex"), new WorldGeneratorType(false, OverworldChunkGeneratorConfig::new));
	}

	private static void addDefaultBiomes() {
		//holy shit this code is still cursed

		// TODO Re-Add Mod Compat
		if (SimplexTerrain.CONFIG.doModCompat) {
			/*
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
			*/
		}

		addVanillaLowlands();
		addVanillaMidlands();
		addVanillaHighlands();
		addVanillaMountainPeaks();
	}

	private static void addVanillaLowlands() {
		final double swampWeight = 1.3;
		final double desertWeight = 1.3;
		final double jungleWeight = 1.5;
		final double savannahWeight = 1.2;
		final double plainsWeight = 1.0;
		final double forestWeight = 1.0;
		final double taigaWeight = 1.2;
		final double birchForestWeight = 0.3;
		final double snowyTaigaWeight = 1.4;
		final double snowyTundraWeight = 0.6;
		final double iceSpikesWeight = 0.4;

		SimplexBiomes.addLowlandsBiome(biomeId(Biomes.DESERT), SimplexClimate.DRY_TROPICAL, desertWeight);

		SimplexBiomes.addLowlandsBiome(biomeId(Biomes.SAVANNA), SimplexClimate.TROPICAL, savannahWeight);
		SimplexBiomes.addLowlandsBiome(PLAINS, SimplexClimate.TROPICAL, plainsWeight);

		SimplexBiomes.addLowlandsBiome(JUNGLE, SimplexClimate.LUSH_TROPICAL, jungleWeight);
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
		SimplexBiomes.addLowlandsBiome(BIRCH_FOREST, SimplexClimate.BOREAL, birchForestWeight);

		SimplexBiomes.addLowlandsBiome(TAIGA, SimplexClimate.LUSH_BOREAL, taigaWeight);

		SimplexBiomes.addLowlandsBiome(biomeId(Biomes.SNOWY_TAIGA), SimplexClimate.SNOWY, snowyTaigaWeight);
		SimplexBiomes.addLowlandsBiome(biomeId(Biomes.SNOWY_TUNDRA), SimplexClimate.SNOWY, snowyTundraWeight);
		SimplexBiomes.addLowlandsBiome(biomeId(Biomes.ICE_SPIKES), SimplexClimate.SNOWY, iceSpikesWeight);
	}

	private static void addVanillaMidlands() {
		final double jungleWeight = 1.0;
		final double savannaPlateauWeight = 1.0;
		final double birchForestWeight = 0.8;
		final double darkForestWeight = 1.0;
		final double forestWeight = 1.5;
		final double plainsWeight = 0.6;
		final double swampWeight = 0.4;
		final double taigaWeight = 0.9;
		final double snowyTaigaWeight = 1.0;
		final double snowyTundraWeight = 1.0;

		SimplexBiomes.addMidlandsBiome(biomeId(Biomes.SAVANNA_PLATEAU), SimplexClimate.DRY_TROPICAL, savannaPlateauWeight);

		SimplexBiomes.addMidlandsBiome(PLAINS, SimplexClimate.TROPICAL, plainsWeight * 1.6);
		SimplexBiomes.addMidlandsBiome(biomeId(Biomes.SAVANNA_PLATEAU), SimplexClimate.TROPICAL, savannaPlateauWeight);

		SimplexBiomes.addMidlandsBiome(JUNGLE, SimplexClimate.LUSH_TROPICAL, jungleWeight);
		SimplexBiomes.addMidlandsBiome(biomeId(Biomes.DARK_FOREST), SimplexClimate.LUSH_TROPICAL, darkForestWeight);

		SimplexBiomes.addMidlandsBiome(BIRCH_FOREST, SimplexClimate.TEMPERATE, birchForestWeight);
		SimplexBiomes.addMidlandsBiome(PLAINS, SimplexClimate.TEMPERATE, plainsWeight * 0.5);

		SimplexBiomes.addMidlandsBiome(BIRCH_FOREST, SimplexClimate.TEMPERATE, birchForestWeight);
		SimplexBiomes.addMidlandsBiome(FOREST, SimplexClimate.TEMPERATE, forestWeight);
		SimplexBiomes.addMidlandsBiome(PLAINS, SimplexClimate.TEMPERATE, plainsWeight);

		SimplexBiomes.addMidlandsBiome(biomeId(Biomes.DARK_FOREST), SimplexClimate.LUSH_TEMPERATE, darkForestWeight);
		SimplexBiomes.addMidlandsBiome(FOREST, SimplexClimate.LUSH_TEMPERATE, forestWeight);
		SimplexBiomes.addMidlandsBiome(SWAMP, SimplexClimate.LUSH_TEMPERATE, swampWeight);

		SimplexBiomes.addMidlandsBiome(TAIGA, SimplexClimate.DRY_BOREAL, birchForestWeight);

		SimplexBiomes.addMidlandsBiome(BIRCH_FOREST, SimplexClimate.BOREAL, birchForestWeight);
		SimplexBiomes.addMidlandsBiome(TAIGA, SimplexClimate.BOREAL, taigaWeight);

		SimplexBiomes.addMidlandsBiome(BIRCH_FOREST, SimplexClimate.LUSH_BOREAL, birchForestWeight * 1.5);
		SimplexBiomes.addMidlandsBiome(FOREST, SimplexClimate.LUSH_BOREAL, forestWeight * 0.5);
		SimplexBiomes.addMidlandsBiome(TAIGA, SimplexClimate.LUSH_BOREAL, taigaWeight);

		SimplexBiomes.addMidlandsBiome(biomeId(Biomes.SNOWY_TAIGA), SimplexClimate.SNOWY, snowyTaigaWeight);
		SimplexBiomes.addMidlandsBiome(biomeId(Biomes.SNOWY_TUNDRA), SimplexClimate.SNOWY, snowyTundraWeight);
	}

	private static void addVanillaHighlands() {
		final double plainsWeight = 1.0;
		final double jungleWeight = 0.4;
		final double savannaPlateauMWeight = 0.4;
		final double mountainEdgeWeight = 1.0;
		final double woodedMountainsWeight = 1.0;
		final double taigaWeight = 0.8;
		final double snowyTaigaWeight = 0.6;
		
		SimplexBiomes.addHighlandsBiome(PLAINS, SimplexClimate.DRY_TROPICAL, plainsWeight);
		SimplexBiomes.addHighlandsBiome(biomeId(Biomes.SHATTERED_SAVANNA_PLATEAU), SimplexClimate.DRY_TROPICAL, savannaPlateauMWeight);

		SimplexBiomes.addHighlandsBiome(PLAINS, SimplexClimate.TROPICAL, plainsWeight);

		SimplexBiomes.addHighlandsBiome(JUNGLE, SimplexClimate.LUSH_TROPICAL, jungleWeight);
		SimplexBiomes.addHighlandsBiome(PLAINS, SimplexClimate.LUSH_TROPICAL, plainsWeight);

		SimplexBiomes.addHighlandsBiome(WOODED_MOUNTAINS, SimplexClimate.DRY_TEMPERATE, woodedMountainsWeight);
		SimplexBiomes.addHighlandsBiome(MOUNTAIN_EDGE, SimplexClimate.DRY_TEMPERATE, mountainEdgeWeight);

		SimplexBiomes.addHighlandsBiome(MOUNTAIN_EDGE, SimplexClimate.TEMPERATE, mountainEdgeWeight);

		SimplexBiomes.addHighlandsBiome(MOUNTAIN_EDGE, SimplexClimate.LUSH_TEMPERATE, mountainEdgeWeight);

		SimplexBiomes.addHighlandsBiome(WOODED_MOUNTAINS, SimplexClimate.DRY_BOREAL, woodedMountainsWeight);
		SimplexBiomes.addHighlandsBiome(MOUNTAIN_EDGE, SimplexClimate.DRY_BOREAL, mountainEdgeWeight * 1.5);
		SimplexBiomes.addHighlandsBiome(TAIGA, SimplexClimate.DRY_BOREAL, taigaWeight);

		SimplexBiomes.addHighlandsBiome(WOODED_MOUNTAINS, SimplexClimate.BOREAL, woodedMountainsWeight);
		SimplexBiomes.addHighlandsBiome(MOUNTAIN_EDGE, SimplexClimate.BOREAL, mountainEdgeWeight);
		SimplexBiomes.addHighlandsBiome(TAIGA, SimplexClimate.BOREAL, taigaWeight);

		SimplexBiomes.addHighlandsBiome(WOODED_MOUNTAINS, SimplexClimate.LUSH_BOREAL, woodedMountainsWeight);
		SimplexBiomes.addHighlandsBiome(TAIGA, SimplexClimate.LUSH_BOREAL, taigaWeight);

		SimplexBiomes.addHighlandsBiome(MOUNTAIN_EDGE, SimplexClimate.SNOWY, mountainEdgeWeight);
		SimplexBiomes.addHighlandsBiome(biomeId(Biomes.SNOWY_TAIGA), SimplexClimate.SNOWY, snowyTaigaWeight);
	}
	
	private static void addVanillaMountainPeaks() {
		final double gravellyMountainsWeight = 0.6;
		final double mountainsWeight = 1.0;
		final double plainsWeight = 1.0;
		
		SimplexBiomes.addMountainPeaksBiome(PLAINS, SimplexClimate.DRY_TROPICAL, plainsWeight);

		SimplexBiomes.addMountainPeaksBiome(PLAINS, SimplexClimate.TROPICAL, plainsWeight);
		SimplexBiomes.addMountainPeaksBiome(GRAVELLY_MOUNTAINS, SimplexClimate.TROPICAL, gravellyMountainsWeight);

		SimplexBiomes.addMountainPeaksBiome(PLAINS, SimplexClimate.LUSH_TROPICAL, plainsWeight);
		SimplexBiomes.addMountainPeaksBiome(GRAVELLY_MOUNTAINS, SimplexClimate.TROPICAL, gravellyMountainsWeight * 0.4);

		SimplexBiomes.addMountainPeaksBiome(GRAVELLY_MOUNTAINS, SimplexClimate.DRY_TEMPERATE, gravellyMountainsWeight);

		SimplexBiomes.addMountainPeaksBiome(GRAVELLY_MOUNTAINS, SimplexClimate.TEMPERATE, gravellyMountainsWeight);
		SimplexBiomes.addMountainPeaksBiome(MOUNTAINS, SimplexClimate.TEMPERATE, mountainsWeight);

		SimplexBiomes.addMountainPeaksBiome(MOUNTAINS, SimplexClimate.LUSH_TEMPERATE, mountainsWeight);

		SimplexBiomes.addMountainPeaksBiome(MOUNTAINS, SimplexClimate.DRY_BOREAL, mountainsWeight);

		SimplexBiomes.addMountainPeaksBiome(MOUNTAINS, SimplexClimate.BOREAL, mountainsWeight);

		SimplexBiomes.addMountainPeaksBiome(MOUNTAINS, SimplexClimate.LUSH_BOREAL, mountainsWeight);

		SimplexBiomes.addMountainPeaksBiome(MOUNTAINS, SimplexClimate.SNOWY, mountainsWeight);
	}

	private static final Identifier biomeId(Biome biome) {
		return Registry.BIOME.getId(biome);
	}
}
