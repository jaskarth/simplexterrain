package supercoder79.simplexterrain.compat;

import net.minecraft.util.Identifier;
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
}
