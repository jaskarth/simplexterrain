package supercoder79.simplexterrain.compat;

import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biomes;
import supercoder79.simplexterrain.api.biomes.SimplexBiomes;
import supercoder79.simplexterrain.api.biomes.SimplexClimate;

public class Compat {
    public static void addTraverseBiomes() {
        double woodLandsWeight = 1.0;
        double autumnalWoodsWeight = 0.5;
        double rollingHillsWeight = 0.6;
        double plateauWeight = 0.5; //Both plateaus
        double aridHighlandsWeight = 1.0;
        double cliffsWeight = 1.0;
        double coniferousForestWeight = 1.0;
        double lushSwampWeight = 1.0;
        double shrublandWeight = 1.0;
        double miniJungleWeight = 1.0;

        //Lowlands Biomes

        SimplexBiomes.addLowlandsBiome(new Identifier("traverse", "woodlands"), SimplexClimate.TEMPERATE, woodLandsWeight*0.9);
        SimplexBiomes.addLowlandsBiome(new Identifier("traverse", "woodlands"), SimplexClimate.LUSH_TEMPERATE, woodLandsWeight*0.5);
        SimplexBiomes.addLowlandsBiome(new Identifier("traverse", "woodlands"), SimplexClimate.DRY_TEMPERATE, woodLandsWeight);

        SimplexBiomes.addLowlandsBiome(new Identifier("traverse", "autumnal_woods"), SimplexClimate.TEMPERATE, autumnalWoodsWeight*0.9);
        SimplexBiomes.addLowlandsBiome(new Identifier("traverse", "autumnal_woods"), SimplexClimate.LUSH_TEMPERATE, autumnalWoodsWeight*0.5);
        SimplexBiomes.addLowlandsBiome(new Identifier("traverse", "autumnal_woods"), SimplexClimate.DRY_TEMPERATE, autumnalWoodsWeight);

        SimplexBiomes.addLowlandsBiome(new Identifier("traverse", "desert_shrubland"), SimplexClimate.TEMPERATE, shrublandWeight*0.9);
        SimplexBiomes.addLowlandsBiome(new Identifier("traverse", "desert_shrubland"), SimplexClimate.DRY_TEMPERATE, shrublandWeight);

        SimplexBiomes.addLowlandsBiome(new Identifier("traverse", "rolling_hills"), SimplexClimate.TEMPERATE, rollingHillsWeight*0.75);
        SimplexBiomes.addLowlandsBiome(new Identifier("traverse", "rolling_hills"), SimplexClimate.LUSH_TEMPERATE, rollingHillsWeight*0.5);
        SimplexBiomes.addLowlandsBiome(new Identifier("traverse", "rolling_hills"), SimplexClimate.TROPICAL, rollingHillsWeight*1);
        SimplexBiomes.addLowlandsBiome(new Identifier("traverse", "rolling_hills"), SimplexClimate.LUSH_TROPICAL, rollingHillsWeight*1.25);

        SimplexBiomes.addLowlandsBiome(new Identifier("traverse", "meadow"), SimplexClimate.TEMPERATE, rollingHillsWeight*0.75);
        SimplexBiomes.addLowlandsBiome(new Identifier("traverse", "meadow"), SimplexClimate.LUSH_TEMPERATE, rollingHillsWeight*0.5);

        SimplexBiomes.addLowlandsBiome(new Identifier("traverse", "wooded_plateau"), SimplexClimate.TEMPERATE, plateauWeight*0.8);
        SimplexBiomes.addLowlandsBiome(new Identifier("traverse", "plains_plateau"), SimplexClimate.TEMPERATE, plateauWeight*0.8);
        SimplexBiomes.addLowlandsBiome(new Identifier("traverse", "wooded_plateau"), SimplexClimate.DRY_TEMPERATE, plateauWeight*0.65);
        SimplexBiomes.addLowlandsBiome(new Identifier("traverse", "plains_plateau"), SimplexClimate.DRY_TEMPERATE, plateauWeight*0.65);
        SimplexBiomes.addLowlandsBiome(new Identifier("traverse", "wooded_plateau"), SimplexClimate.LUSH_TEMPERATE, plateauWeight);
        SimplexBiomes.addLowlandsBiome(new Identifier("traverse", "plains_plateau"), SimplexClimate.LUSH_TEMPERATE, plateauWeight);

        SimplexBiomes.addLowlandsBiome(new Identifier("traverse", "arid_highlands"), SimplexClimate.DRY_TEMPERATE, aridHighlandsWeight);
        SimplexBiomes.addLowlandsBiome(new Identifier("traverse", "arid_highlands"), SimplexClimate.TEMPERATE, aridHighlandsWeight*0.8);
        SimplexBiomes.addLowlandsBiome(new Identifier("traverse", "arid_highlands"), SimplexClimate.LUSH_TEMPERATE, aridHighlandsWeight*0.6);

        SimplexBiomes.addLowlandsBiome(new Identifier("traverse", "lush_swamp"), SimplexClimate.TROPICAL, lushSwampWeight*1);
        SimplexBiomes.addLowlandsBiome(new Identifier("traverse", "lush_swamp"), SimplexClimate.LUSH_TROPICAL, lushSwampWeight*1.25);
        SimplexBiomes.addLowlandsBiome(new Identifier("traverse", "lush_swamp"), SimplexClimate.DRY_TROPICAL, lushSwampWeight*0.75);

        SimplexBiomes.addLowlandsBiome(new Identifier("traverse", "mini_jungle"), SimplexClimate.TROPICAL, miniJungleWeight*1);
        SimplexBiomes.addLowlandsBiome(new Identifier("traverse", "mini_jungle"), SimplexClimate.LUSH_TROPICAL, miniJungleWeight*1.25);
        SimplexBiomes.addLowlandsBiome(new Identifier("traverse", "mini_jungle"), SimplexClimate.DRY_TROPICAL, miniJungleWeight*0.75);

        SimplexBiomes.addLowlandsBiome(new Identifier("traverse", "coniferous_forest"), SimplexClimate.DRY_BOREAL, coniferousForestWeight*0.6);
        SimplexBiomes.addLowlandsBiome(new Identifier("traverse", "coniferous_forest"), SimplexClimate.BOREAL, coniferousForestWeight);
        SimplexBiomes.addLowlandsBiome(new Identifier("traverse", "coniferous_forest"), SimplexClimate.LUSH_BOREAL, coniferousForestWeight*1.3);

        SimplexBiomes.addLowlandsBiome(new Identifier("traverse", "snowy_coniferous_forest"), SimplexClimate.SNOWY, coniferousForestWeight*0.6);

        //Midlands Biomes

        SimplexBiomes.addMidlandsBiome(new Identifier("traverse", "woodlands"), SimplexClimate.TEMPERATE, woodLandsWeight);
        SimplexBiomes.addMidlandsBiome(new Identifier("traverse", "woodlands"), SimplexClimate.LUSH_TEMPERATE, woodLandsWeight*0.6);
        SimplexBiomes.addMidlandsBiome(new Identifier("traverse", "woodlands"), SimplexClimate.DRY_TEMPERATE, woodLandsWeight*1.2);

        SimplexBiomes.addLowlandsBiome(new Identifier("traverse", "autumnal_woods"), SimplexClimate.TEMPERATE, autumnalWoodsWeight*0.9);
        SimplexBiomes.addLowlandsBiome(new Identifier("traverse", "autumnal_woods"), SimplexClimate.LUSH_TEMPERATE, autumnalWoodsWeight*0.5);
        SimplexBiomes.addLowlandsBiome(new Identifier("traverse", "autumnal_woods"), SimplexClimate.DRY_TEMPERATE, autumnalWoodsWeight);

        SimplexBiomes.addMidlandsBiome(new Identifier("traverse", "rolling_hills"), SimplexClimate.TEMPERATE, rollingHillsWeight*0.8);
        SimplexBiomes.addMidlandsBiome(new Identifier("traverse", "rolling_hills"), SimplexClimate.LUSH_TEMPERATE, rollingHillsWeight*0.6);
        SimplexBiomes.addMidlandsBiome(new Identifier("traverse", "rolling_hills"), SimplexClimate.TROPICAL, rollingHillsWeight*1.2);
        SimplexBiomes.addMidlandsBiome(new Identifier("traverse", "rolling_hills"), SimplexClimate.LUSH_TROPICAL, rollingHillsWeight*1.4);

        SimplexBiomes.addMidlandsBiome(new Identifier("traverse", "meadow"), SimplexClimate.TEMPERATE, rollingHillsWeight*0.8);
        SimplexBiomes.addMidlandsBiome(new Identifier("traverse", "meadow"), SimplexClimate.LUSH_TEMPERATE, rollingHillsWeight*0.6);

        SimplexBiomes.addMidlandsBiome(new Identifier("traverse", "wooded_plateau"), SimplexClimate.TEMPERATE, plateauWeight*0.6);
        SimplexBiomes.addMidlandsBiome(new Identifier("traverse", "plains_plateau"), SimplexClimate.TEMPERATE, plateauWeight*0.6);
        SimplexBiomes.addMidlandsBiome(new Identifier("traverse", "wooded_plateau"), SimplexClimate.DRY_TEMPERATE, plateauWeight*0.55);
        SimplexBiomes.addMidlandsBiome(new Identifier("traverse", "plains_plateau"), SimplexClimate.DRY_TEMPERATE, plateauWeight*0.55);
        SimplexBiomes.addMidlandsBiome(new Identifier("traverse", "wooded_plateau"), SimplexClimate.LUSH_TEMPERATE, plateauWeight*0.65);
        SimplexBiomes.addMidlandsBiome(new Identifier("traverse", "plains_plateau"), SimplexClimate.LUSH_TEMPERATE, plateauWeight*0.65);

        SimplexBiomes.addMidlandsBiome(new Identifier("traverse", "coniferous_forest"), SimplexClimate.DRY_BOREAL, coniferousForestWeight*0.6);
        SimplexBiomes.addMidlandsBiome(new Identifier("traverse", "coniferous_forest"), SimplexClimate.BOREAL, coniferousForestWeight);
        SimplexBiomes.addMidlandsBiome(new Identifier("traverse", "coniferous_forest"), SimplexClimate.LUSH_BOREAL, coniferousForestWeight*1.3);

        SimplexBiomes.addMidlandsBiome(new Identifier("traverse", "lush_swamp"), SimplexClimate.TROPICAL, lushSwampWeight*0.5);
        SimplexBiomes.addMidlandsBiome(new Identifier("traverse", "lush_swamp"), SimplexClimate.LUSH_TROPICAL, lushSwampWeight*0.75);
        SimplexBiomes.addMidlandsBiome(new Identifier("traverse", "lush_swamp"), SimplexClimate.DRY_TROPICAL, lushSwampWeight*0.25);

        SimplexBiomes.addMidlandsBiome(new Identifier("traverse", "snowy_coniferous_forest"), SimplexClimate.SNOWY, coniferousForestWeight*0.6);

        //Highlands Biomes
        SimplexBiomes.addHighlandsBiome(new Identifier("traverse", "cliffs"), SimplexClimate.BOREAL, cliffsWeight);
        SimplexBiomes.addHighlandsBiome(new Identifier("traverse", "cliffs"), SimplexClimate.DRY_BOREAL, cliffsWeight);

        SimplexBiomes.addHighlandsBiome(new Identifier("traverse", "snowy_coniferous_forest"), SimplexClimate.DRY_BOREAL, coniferousForestWeight*0.6);
        SimplexBiomes.addHighlandsBiome(new Identifier("traverse", "snowy_coniferous_forest"), SimplexClimate.BOREAL, coniferousForestWeight);
        SimplexBiomes.addHighlandsBiome(new Identifier("traverse", "snowy_coniferous_forest"), SimplexClimate.LUSH_BOREAL, coniferousForestWeight*1.3);

        SimplexBiomes.addHighlandsBiome(new Identifier("traverse", "snowy_coniferous_forest"), SimplexClimate.SNOWY, coniferousForestWeight*0.6);
    }

    public static void addTerrestriaBiomes() {
        double cypressForestWeight = 1.0;
        double denseWoodlandsWeight = 0.8;
        double cypressSwampWeight = 1.0;
        double hemlockRainforestWeight = 0.7;
        double mapleForestWeight = 0.9;
        double sakuraForestWeight = 0.9;
        double redwoodRainforestWeight = 0.85;
        double rainbowRainforestWeight = 0.9;
        double redwoodForestWeight = 1.0;
        double snowyhemlockForestWeight = 1.0;
        double desertWeight = 1.0;

        //Lowlands biomes

        SimplexBiomes.addLowlandsBiome(new Identifier("terrestria", "cypress_forest"), SimplexClimate.TEMPERATE, cypressForestWeight*0.9);
        SimplexBiomes.addLowlandsBiome(new Identifier("terrestria", "cypress_forest"), SimplexClimate.LUSH_TEMPERATE, cypressForestWeight);
        SimplexBiomes.addLowlandsBiome(new Identifier("terrestria", "cypress_forest"), SimplexClimate.DRY_TEMPERATE, cypressForestWeight*0.5);

        SimplexBiomes.addMidlandsBiome(new Identifier("terrestria", "dense_woodlands"), SimplexClimate.TEMPERATE, denseWoodlandsWeight*0.9);
        SimplexBiomes.addMidlandsBiome(new Identifier("terrestria", "dense_woodlands"), SimplexClimate.LUSH_TEMPERATE, denseWoodlandsWeight*0.65);
        SimplexBiomes.addMidlandsBiome(new Identifier("terrestria", "dense_woodlands"), SimplexClimate.DRY_TEMPERATE, denseWoodlandsWeight*0.8);

        SimplexBiomes.addLowlandsBiome(new Identifier("terrestria", "cypress_swamp"), SimplexClimate.TROPICAL, cypressSwampWeight*1);
        SimplexBiomes.addLowlandsBiome(new Identifier("terrestria", "cypress_swamp"), SimplexClimate.LUSH_TROPICAL, cypressSwampWeight*1.25);
        SimplexBiomes.addLowlandsBiome(new Identifier("terrestria", "cypress_swamp"), SimplexClimate.DRY_TROPICAL, cypressSwampWeight*0.75);

        SimplexBiomes.addLowlandsBiome(new Identifier("terrestria", "hemlock_rainforest"), SimplexClimate.BOREAL, hemlockRainforestWeight*1);
        SimplexBiomes.addLowlandsBiome(new Identifier("terrestria", "hemlock_rainforest"), SimplexClimate.LUSH_BOREAL, hemlockRainforestWeight*1.25);
        SimplexBiomes.addLowlandsBiome(new Identifier("terrestria", "hemlock_rainforest"), SimplexClimate.DRY_BOREAL, hemlockRainforestWeight*0.75);

        SimplexBiomes.addLowlandsBiome(new Identifier("terrestria", "lush_redwood_forest"), SimplexClimate.TEMPERATE, redwoodRainforestWeight*1);
        SimplexBiomes.addLowlandsBiome(new Identifier("terrestria", "lush_redwood_forest"), SimplexClimate.LUSH_TEMPERATE, redwoodRainforestWeight*1.25);
        SimplexBiomes.addLowlandsBiome(new Identifier("terrestria", "lush_redwood_forest"), SimplexClimate.DRY_TEMPERATE, redwoodRainforestWeight*0.75);

        SimplexBiomes.addLowlandsBiome(new Identifier("terrestria", "rainbow_rainforest"), SimplexClimate.LUSH_TROPICAL, rainbowRainforestWeight*1.25);

        SimplexBiomes.addLowlandsBiome(new Identifier("terrestria", "redwood_forest"), SimplexClimate.TEMPERATE, redwoodForestWeight*1);
        SimplexBiomes.addLowlandsBiome(new Identifier("terrestria", "redwood_forest"), SimplexClimate.LUSH_TEMPERATE, redwoodForestWeight*1.25);
        SimplexBiomes.addLowlandsBiome(new Identifier("terrestria", "redwood_forest"), SimplexClimate.DRY_TEMPERATE, redwoodForestWeight*0.75);

        SimplexBiomes.addLowlandsBiome(new Identifier("terrestria", "japanese_maple_forest"), SimplexClimate.TEMPERATE, mapleForestWeight*0.9);
        SimplexBiomes.addLowlandsBiome(new Identifier("terrestria", "japanese_maple_forest"), SimplexClimate.LUSH_TEMPERATE, mapleForestWeight*0.65);
        SimplexBiomes.addLowlandsBiome(new Identifier("terrestria", "japanese_maple_forest"), SimplexClimate.DRY_TEMPERATE, mapleForestWeight*0.8);

        SimplexBiomes.addLowlandsBiome(new Identifier("terrestria", "sakura_forest"), SimplexClimate.TEMPERATE, sakuraForestWeight*0.9);
        SimplexBiomes.addLowlandsBiome(new Identifier("terrestria", "sakura_forest"), SimplexClimate.LUSH_TEMPERATE, sakuraForestWeight*0.65);
        SimplexBiomes.addLowlandsBiome(new Identifier("terrestria", "sakura_forest"), SimplexClimate.DRY_TEMPERATE, sakuraForestWeight*0.8);

        SimplexBiomes.addLowlandsBiome(new Identifier("terrestria", "lush_desert"), SimplexClimate.DRY_BOREAL, desertWeight*1.2);
        SimplexBiomes.addLowlandsBiome(new Identifier("terrestria", "lush_desert"), SimplexClimate.DRY_TEMPERATE, desertWeight);
        SimplexBiomes.addLowlandsBiome(new Identifier("terrestria", "lush_desert"), SimplexClimate.DRY_TROPICAL, desertWeight*0.5);

        //Midlands biomes

        SimplexBiomes.addMidlandsBiome(new Identifier("terrestria", "cypress_forest"), SimplexClimate.TEMPERATE, denseWoodlandsWeight*0.9);
        SimplexBiomes.addMidlandsBiome(new Identifier("terrestria", "cypress_forest"), SimplexClimate.LUSH_TEMPERATE, denseWoodlandsWeight);
        SimplexBiomes.addMidlandsBiome(new Identifier("terrestria", "cypress_forest"), SimplexClimate.DRY_TEMPERATE, denseWoodlandsWeight*0.5);

        SimplexBiomes.addMidlandsBiome(new Identifier("terrestria", "dense_woodlands"), SimplexClimate.TEMPERATE, denseWoodlandsWeight*0.9);
        SimplexBiomes.addMidlandsBiome(new Identifier("terrestria", "dense_woodlands"), SimplexClimate.LUSH_TEMPERATE, denseWoodlandsWeight*0.65);
        SimplexBiomes.addMidlandsBiome(new Identifier("terrestria", "dense_woodlands"), SimplexClimate.DRY_TEMPERATE, denseWoodlandsWeight*0.8);

        SimplexBiomes.addMidlandsBiome(new Identifier("terrestria", "cypress_swamp"), SimplexClimate.TROPICAL, cypressSwampWeight*0.5);
        SimplexBiomes.addMidlandsBiome(new Identifier("terrestria", "cypress_swamp"), SimplexClimate.LUSH_TROPICAL, cypressSwampWeight*0.75);
        SimplexBiomes.addMidlandsBiome(new Identifier("terrestria", "cypress_swamp"), SimplexClimate.DRY_TROPICAL, cypressSwampWeight*0.25);

        SimplexBiomes.addMidlandsBiome(new Identifier("terrestria", "hemlock_rainforest"), SimplexClimate.BOREAL, hemlockRainforestWeight*0.75);
        SimplexBiomes.addMidlandsBiome(new Identifier("terrestria", "hemlock_rainforest"), SimplexClimate.LUSH_BOREAL, hemlockRainforestWeight*1);
        SimplexBiomes.addMidlandsBiome(new Identifier("terrestria", "hemlock_rainforest"), SimplexClimate.DRY_BOREAL, hemlockRainforestWeight*0.5);

        SimplexBiomes.addMidlandsBiome(new Identifier("terrestria", "lush_redwood_forest"), SimplexClimate.TEMPERATE, redwoodRainforestWeight*0.75);
        SimplexBiomes.addMidlandsBiome(new Identifier("terrestria", "lush_redwood_forest"), SimplexClimate.LUSH_TEMPERATE, redwoodRainforestWeight*1);
        SimplexBiomes.addMidlandsBiome(new Identifier("terrestria", "lush_redwood_forest"), SimplexClimate.DRY_TEMPERATE, redwoodRainforestWeight*0.5);

        SimplexBiomes.addMidlandsBiome(new Identifier("terrestria", "rainbow_rainforest"), SimplexClimate.LUSH_TROPICAL, rainbowRainforestWeight*1.25);

        SimplexBiomes.addMidlandsBiome(new Identifier("terrestria", "redwood_forest"), SimplexClimate.TEMPERATE, redwoodForestWeight*0.75);
        SimplexBiomes.addMidlandsBiome(new Identifier("terrestria", "redwood_forest"), SimplexClimate.LUSH_TEMPERATE, redwoodForestWeight*1);
        SimplexBiomes.addMidlandsBiome(new Identifier("terrestria", "redwood_forest"), SimplexClimate.DRY_TEMPERATE, redwoodForestWeight*0.5);

        SimplexBiomes.addMidlandsBiome(new Identifier("terrestria", "japanese_maple_forest"), SimplexClimate.TEMPERATE, mapleForestWeight*0.9);
        SimplexBiomes.addMidlandsBiome(new Identifier("terrestria", "japanese_maple_forest"), SimplexClimate.LUSH_TEMPERATE, mapleForestWeight*0.65);
        SimplexBiomes.addMidlandsBiome(new Identifier("terrestria", "japanese_maple_forest"), SimplexClimate.DRY_TEMPERATE, mapleForestWeight*0.8);

        SimplexBiomes.addMidlandsBiome(new Identifier("terrestria", "sakura_forest"), SimplexClimate.TEMPERATE, sakuraForestWeight*0.9);
        SimplexBiomes.addMidlandsBiome(new Identifier("terrestria", "sakura_forest"), SimplexClimate.LUSH_TEMPERATE, sakuraForestWeight*0.65);
        SimplexBiomes.addMidlandsBiome(new Identifier("terrestria", "sakura_forest"), SimplexClimate.DRY_TEMPERATE, sakuraForestWeight*0.8);

        SimplexBiomes.addMidlandsBiome(new Identifier("terrestria", "lush_desert"), SimplexClimate.DRY_BOREAL, desertWeight*1);
        SimplexBiomes.addMidlandsBiome(new Identifier("terrestria", "lush_desert"), SimplexClimate.DRY_TEMPERATE, desertWeight*0.9);
        SimplexBiomes.addMidlandsBiome(new Identifier("terrestria", "lush_desert"), SimplexClimate.DRY_TROPICAL, desertWeight*0.35);

        //Highlands biomes
        SimplexBiomes.addHighlandsBiome(new Identifier("terrestria", "snowy_hemlock_forest"), SimplexClimate.TEMPERATE, snowyhemlockForestWeight*0.9);
        SimplexBiomes.addHighlandsBiome(new Identifier("terrestria", "snowy_hemlock_forest"), SimplexClimate.LUSH_TEMPERATE, snowyhemlockForestWeight*0.65);
        SimplexBiomes.addHighlandsBiome(new Identifier("terrestria", "snowy_hemlock_forest"), SimplexClimate.DRY_TEMPERATE, snowyhemlockForestWeight*0.8);

        //replacements

        SimplexBiomes.addReplacementBiome(new Identifier("minecraft", "jungle"), new Identifier("terrestria", "volcanic_island"), 10);
    }

    // Corgi Taco why did you add so many biomes :(
    public static void addBYGBiomes() {
        double fieldsWeight = 0.3;
        addPrimaryLowlandsBiome(byg("allium_fields"), SimplexClimate.TEMPERATE, fieldsWeight);
        addPrimaryLowlandsBiome(byg("allium_fields"), SimplexClimate.LUSH_TEMPERATE, fieldsWeight);
        addPrimaryLowlandsBiome(byg("amaranth_fields"), SimplexClimate.TEMPERATE, fieldsWeight);
        addPrimaryLowlandsBiome(byg("amaranth_fields"), SimplexClimate.LUSH_TEMPERATE, fieldsWeight);
        addPrimaryLowlandsBiome(byg("prairie"), SimplexClimate.DRY_TEMPERATE, fieldsWeight);
        addPrimaryLowlandsBiome(byg("prairie"), SimplexClimate.TEMPERATE, fieldsWeight);
        addPrimaryLowlandsBiome(byg("meadow"), SimplexClimate.DRY_TEMPERATE, fieldsWeight);
        addPrimaryLowlandsBiome(byg("meadow"), SimplexClimate.TEMPERATE, fieldsWeight);

        SimplexBiomes.addLowlandsBiome(byg("cold_swamplands"), SimplexClimate.LUSH_BOREAL, 0.2);
        SimplexBiomes.addLowlandsBiome(byg("bayou"), SimplexClimate.TROPICAL, 0.2);
        SimplexBiomes.addLowlandsBiome(byg("bayou"), SimplexClimate.LUSH_TROPICAL, 0.3);
        SimplexBiomes.addLowlandsBiome(byg("glowshroom_bayou"), SimplexClimate.TROPICAL, 0.1);
        SimplexBiomes.addLowlandsBiome(byg("glowshroom_bayou"), SimplexClimate.LUSH_TROPICAL, 0.1);
        SimplexBiomes.addLowlandsBiome(byg("vibrant_swamplands"), SimplexClimate.TROPICAL, 0.1);
        SimplexBiomes.addLowlandsBiome(byg("vibrant_swamplands"), SimplexClimate.LUSH_TROPICAL, 0.1);
        addPrimaryLowlandsBiome(byg("baobab_savanna"), SimplexClimate.DRY_TROPICAL, 0.2);
        addPrimaryLowlandsBiome(byg("red_desert"), SimplexClimate.DRY_TROPICAL, 0.2);
        addPrimaryLowlandsBiome(byg("dunes"), SimplexClimate.DRY_TROPICAL, 0.2);
        addPrimaryLowlandsBiome(byg("mojave_desert"), SimplexClimate.DRY_TEMPERATE, 0.3);
        addPrimaryLowlandsBiome(byg("grassland_plateau"), SimplexClimate.TEMPERATE, 0.3);
        addPrimaryLowlandsBiome(byg("lush_tundra"), SimplexClimate.SNOWY, 0.4);
        addPrimaryLowlandsBiome(byg("snowy_blue_taiga"), SimplexClimate.SNOWY, 0.3);
        addPrimaryLowlandsBiome(byg("snowy_coniferous_forest"), SimplexClimate.SNOWY, 0.3);
        addPrimaryLowlandsBiome(byg("snowy_deciduous_forest"), SimplexClimate.SNOWY, 0.3);
        addPrimaryLowlandsBiome(byg("snowy_evergreen_taiga"), SimplexClimate.SNOWY, 0.3);
        addPrimaryLowlandsBiome(byg("shrublands"), SimplexClimate.DRY_TROPICAL, 0.3);
        addPrimaryLowlandsBiome(byg("tropical_fungal_forest"), SimplexClimate.LUSH_TROPICAL, 0.2);
        addPrimaryLowlandsBiome(byg("tropical_rainforest"), SimplexClimate.DRY_TROPICAL, 0.3);
        addPrimaryLowlandsBiome(byg("weeping_witch_forest"), SimplexClimate.TEMPERATE, 0.2);
        addPrimaryLowlandsBiome(byg("skyris_highlands"), SimplexClimate.BOREAL, 0.2);


        //TODO: dry temperate
        double forestWeight = 0.2;
        addPrimaryMidlandsBiome(byg("aspen_forest"), SimplexClimate.TEMPERATE, forestWeight);
        addPrimaryMidlandsBiome(byg("aspen_forest"), SimplexClimate.LUSH_TEMPERATE, forestWeight);
        addPrimaryMidlandsBiome(byg("deciduous_forest"), SimplexClimate.TEMPERATE, forestWeight);
        addPrimaryMidlandsBiome(byg("deciduous_forest"), SimplexClimate.LUSH_TEMPERATE, forestWeight);
        addPrimaryMidlandsBiome(byg("jacaranda_forest"), SimplexClimate.TEMPERATE, forestWeight);
        addPrimaryMidlandsBiome(byg("jacaranda_forest"), SimplexClimate.LUSH_TEMPERATE, forestWeight);
        addPrimaryMidlandsBiome(byg("cherry_blossom_forest"), SimplexClimate.TEMPERATE, forestWeight);
        addPrimaryMidlandsBiome(byg("cherry_blossom_forest"), SimplexClimate.LUSH_TEMPERATE, forestWeight);
        addPrimaryMidlandsBiome(byg("autumnal_valley"), SimplexClimate.TEMPERATE, forestWeight);
        addPrimaryMidlandsBiome(byg("autumnal_valley"), SimplexClimate.LUSH_TEMPERATE, forestWeight);
        addPrimaryMidlandsBiome(byg("orchard"), SimplexClimate.TEMPERATE, forestWeight);
        addPrimaryMidlandsBiome(byg("orchard"), SimplexClimate.LUSH_TEMPERATE, forestWeight);
        addPrimaryMidlandsBiome(byg("seasonal_birch_forest"), SimplexClimate.TEMPERATE, forestWeight);
        addPrimaryMidlandsBiome(byg("seasonal_birch_forest"), SimplexClimate.LUSH_TEMPERATE, forestWeight);
        addPrimaryMidlandsBiome(byg("seasonal_deciduous_forest"), SimplexClimate.TEMPERATE, forestWeight);
        addPrimaryMidlandsBiome(byg("seasonal_deciduous_forest"), SimplexClimate.LUSH_TEMPERATE, forestWeight);
        addPrimaryMidlandsBiome(byg("seasonal_forest"), SimplexClimate.TEMPERATE, forestWeight);
        addPrimaryMidlandsBiome(byg("seasonal_forest"), SimplexClimate.LUSH_TEMPERATE, forestWeight);
        addPrimaryMidlandsBiome(byg("woodlands"), SimplexClimate.TEMPERATE, forestWeight);
        addPrimaryMidlandsBiome(byg("woodlands"), SimplexClimate.LUSH_TEMPERATE, forestWeight);

        addPrimaryMidlandsBiome(byg("boreal_forest"), SimplexClimate.DRY_BOREAL, forestWeight * 0.5);
        addPrimaryMidlandsBiome(byg("boreal_forest"), SimplexClimate.BOREAL, forestWeight);
        addPrimaryMidlandsBiome(byg("boreal_forest"), SimplexClimate.LUSH_BOREAL, forestWeight);
        addPrimaryMidlandsBiome(byg("coniferous_forest"), SimplexClimate.DRY_BOREAL, forestWeight * 0.5);
        addPrimaryMidlandsBiome(byg("coniferous_forest"), SimplexClimate.BOREAL, forestWeight);
        addPrimaryMidlandsBiome(byg("coniferous_forest"), SimplexClimate.LUSH_BOREAL, forestWeight);
        addPrimaryMidlandsBiome(byg("blue_taiga"), SimplexClimate.DRY_BOREAL, forestWeight * 0.5);
        addPrimaryMidlandsBiome(byg("blue_taiga"), SimplexClimate.BOREAL, forestWeight);
        addPrimaryMidlandsBiome(byg("blue_taiga"), SimplexClimate.LUSH_BOREAL, forestWeight);
        addPrimaryMidlandsBiome(byg("evergreen_taiga"), SimplexClimate.DRY_BOREAL, forestWeight);
        addPrimaryMidlandsBiome(byg("evergreen_taiga"), SimplexClimate.BOREAL, forestWeight);
        addPrimaryMidlandsBiome(byg("grove"), SimplexClimate.DRY_BOREAL, forestWeight);
        addPrimaryMidlandsBiome(byg("grove"), SimplexClimate.BOREAL, forestWeight);
        addPrimaryMidlandsBiome(byg("maple_taiga"), SimplexClimate.BOREAL, forestWeight);
        addPrimaryMidlandsBiome(byg("maple_taiga"), SimplexClimate.LUSH_BOREAL, forestWeight);
        addPrimaryMidlandsBiome(byg("guiana_shield"), SimplexClimate.DRY_BOREAL, forestWeight);
        addPrimaryMidlandsBiome(byg("guiana_shield"), SimplexClimate.BOREAL, forestWeight);
        addPrimaryMidlandsBiome(byg("guiana_shield"), SimplexClimate.LUSH_BOREAL, forestWeight);
        addPrimaryMidlandsBiome(byg("seasonal_taiga"), SimplexClimate.DRY_BOREAL, forestWeight);
        addPrimaryMidlandsBiome(byg("seasonal_taiga"), SimplexClimate.BOREAL, forestWeight);

    }

    private static Identifier byg(String name) {
        return new Identifier("byg", name);
    }

    private static void addPrimaryLowlandsBiome(Identifier id, SimplexClimate climate, double weight) {
        SimplexBiomes.addLowlandsBiome(id, climate, weight);
        SimplexBiomes.addMidlandsBiome(id, climate, weight * 0.5);
    }

    private static void addPrimaryMidlandsBiome(Identifier id, SimplexClimate climate, double weight) {
        SimplexBiomes.addMidlandsBiome(id, climate, weight);
        SimplexBiomes.addLowlandsBiome(id, climate, weight * 0.35);
        SimplexBiomes.addHighlandsBiome(id, climate, weight * 0.35);
    }
}
