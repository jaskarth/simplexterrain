package supercoder79.simplexterrain.api;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds the biomes and the weights for each layer.
 *
 * @author SuperCoder79
 */
public class LandBiomeLayerHolder {
    public static Map<Integer, Integer> lowlandBiomes = new HashMap<>();
    public static Map<Integer, Integer> midlandBiomes = new HashMap<>();
    public static Map<Integer, Integer> highlandBiomes = new HashMap<>();
    public static Map<Integer, Integer> toplandBiomes = new HashMap<>();

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

        //mod compat
        if (FabricLoader.getInstance().isModLoaded("winterbiomemod")) {
            addToHighlands(Registry.BIOME.get(new Identifier("winterbiomemod", "white_oaks")), 8);
            addToHighlands(Registry.BIOME.get(new Identifier("winterbiomemod", "white_oaks_thicket")), 10);
            addToHighlands(Registry.BIOME.get(new Identifier("winterbiomemod", "alpine")), 7);
            addToHighlands(Registry.BIOME.get(new Identifier("winterbiomemod", "subalpine")), 10);
            addToHighlands(Registry.BIOME.get(new Identifier("winterbiomemod", "subalpine_crag")), 10);

            addToToplands(Registry.BIOME.get(new Identifier("winterbiomemod", "alpine_peaks")), 8);
            addToToplands(Registry.BIOME.get(new Identifier("winterbiomemod", "alpine_glacier")), 9);

            System.out.println("Winter biomes registered!");
        }

        if (FabricLoader.getInstance().isModLoaded("traverse")) {
            System.out.println("Traverse biomes registered!");
        }

        if (FabricLoader.getInstance().isModLoaded("terrestria")) {
            System.out.println("Terrestria biomes registered!");
        }

        addToLowlands(Biomes.SWAMP, swampWeight);
        addToLowlands(Biomes.DESERT, desertWeight);
        addToLowlands(Biomes.JUNGLE, jungleWeight);
        addToLowlands(Biomes.SAVANNA, savannahWeight);

        addToMidlands(Biomes.BIRCH_FOREST, birchForestWeight);
        addToMidlands(Biomes.DARK_FOREST, darkForestWeight);

        addToHighlands(Biomes.MOUNTAIN_EDGE, mountainEdgeWeight);
        addToHighlands(Biomes.WOODED_MOUNTAINS, woodedMountainsWeight);

        addToToplands(Biomes.GRAVELLY_MOUNTAINS, gravellyMountainsWeight);
        addToToplands(Biomes.WOODED_MOUNTAINS, woodedMountainsToplandsWeight);
        addToToplands(Biomes.ICE_SPIKES, iceSpikesWeight);
    }

    public static void addToLowlands(Biome biome, int weight) {
        lowlandBiomes.put(Registry.BIOME.getRawId(biome), weight);
    }
    public static void addToMidlands(Biome biome, int weight) {
        midlandBiomes.put(Registry.BIOME.getRawId(biome), weight);
    }
    public static void addToHighlands(Biome biome, int weight) {
        highlandBiomes.put(Registry.BIOME.getRawId(biome), weight);
    }
    public static void addToToplands(Biome biome, int weight) {
        toplandBiomes.put(Registry.BIOME.getRawId(biome), weight);
    }
}
