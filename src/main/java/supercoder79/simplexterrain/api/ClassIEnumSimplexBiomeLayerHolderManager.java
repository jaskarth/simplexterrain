package supercoder79.simplexterrain.api;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import supercoder79.simplexterrain.SimplexTerrain;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds the biomes and the weights for each layer.
 *
 * @author SuperCoder79
 */
public final class ClassIEnumSimplexBiomeLayerHolderManager {
    public static final Map<Identifier, Integer> lowlandBiomes = new HashMap<>();
    public static final Map<Identifier, Integer> midlandBiomes = new HashMap<>();
    public static final Map<Identifier, Integer> highlandBiomes = new HashMap<>();
    public static final Map<Identifier, Integer> toplandBiomes = new HashMap<>();

    public static void init() {
        //holy shit this code is cursed

        //lowlands
        int swampWeight = 13;
        int desertWeight = 13;
        int jungleWeight = 15;
        int savannahWeight = 12;

        //midlands
        int birchForestWeight = 8;
        int darkForestWeight = 10;

        //highlands
        int mountainEdgeWeight = 8;
        int woodedMountainsWeight = 8;

        //toplands
        int gravellyMountainsWeight = 6;
        int woodedMountainsToplandsWeight = 7;
        int iceSpikesWeight = 16;

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

        addToLowlands(Registry.BIOME.getId(Biomes.SWAMP), swampWeight);
        addToLowlands(Registry.BIOME.getId(Biomes.DESERT), desertWeight);
        addToLowlands(Registry.BIOME.getId(Biomes.JUNGLE), jungleWeight);
        addToLowlands(Registry.BIOME.getId(Biomes.SAVANNA), savannahWeight);

        addToMidlands(Registry.BIOME.getId(Biomes.BIRCH_FOREST), birchForestWeight);
        addToMidlands(Registry.BIOME.getId(Biomes.DARK_FOREST), darkForestWeight);

        addToHighlands(Registry.BIOME.getId(Biomes.MOUNTAIN_EDGE), mountainEdgeWeight);
        addToHighlands(Registry.BIOME.getId(Biomes.WOODED_MOUNTAINS), woodedMountainsWeight);

        addToToplands(Registry.BIOME.getId(Biomes.GRAVELLY_MOUNTAINS), gravellyMountainsWeight);
        addToToplands(Registry.BIOME.getId(Biomes.WOODED_MOUNTAINS), woodedMountainsToplandsWeight);
        addToToplands(Registry.BIOME.getId(Biomes.ICE_SPIKES), iceSpikesWeight);
    }

    public static void addToLowlands(Identifier biome, int weight) {
        lowlandBiomes.put(biome, weight);
    }
    public static void addToMidlands(Identifier biome, int weight) {
        midlandBiomes.put(biome, weight);
    }
    public static void addToHighlands(Identifier biome, int weight) {
        highlandBiomes.put(biome, weight);
    }
    public static void addToToplands(Identifier biome, int weight) {
        toplandBiomes.put(biome, weight);
    }
}
