package supercoder79.simplexterrain.world.biome;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import supercoder79.simplexterrain.api.biomes.SimplexClimate;

public final class SimplexBiomesImpl {
	private SimplexBiomesImpl() {
	}

	private static final Map<SimplexClimate, BiomePicker> lowlandBiomes = new HashMap<>();
	private static final Map<SimplexClimate, BiomePicker> midlandBiomes = new HashMap<>();
	private static final Map<SimplexClimate, BiomePicker> highlandBiomes = new HashMap<>();
	private static final Map<SimplexClimate, BiomePicker> mountainPeaksBiomes = new HashMap<>();

	private static final Map<Biome, Pair<Biome, Integer>> replacementBiomes = new HashMap<>();

	public static void addToLowlands(Identifier biome, SimplexClimate climate, double weight) {
		lowlandBiomes.computeIfAbsent(climate, simplexClimate -> new BiomePicker()).addBiome(biome, weight);
	}
	public static void addToMidlands(Identifier biome, SimplexClimate climate, double weight) {
		midlandBiomes.computeIfAbsent(climate, simplexClimate -> new BiomePicker()).addBiome(biome, weight);
	}
	public static void addToHighlands(Identifier biome, SimplexClimate climate, double weight) {
		highlandBiomes.computeIfAbsent(climate, simplexClimate -> new BiomePicker()).addBiome(biome, weight);
	}
	public static void addToMountainPeaks(Identifier biome, SimplexClimate climate, double weight) {
		mountainPeaksBiomes.computeIfAbsent(climate, simplexClimate -> new BiomePicker()).addBiome(biome, weight);
	}

	public static void addReplacementBiome(Identifier biomeToReplace, Identifier replacement, int chance) {
		replacementBiomes.put(Registry.BIOME.get(biomeToReplace), new Pair<>(Registry.BIOME.get(replacement), chance));
	}
	
	public static BiomePicker getLowlandsBiomePicker(SimplexClimate climate) {
		return lowlandBiomes.get(climate);
	}
	public static BiomePicker getMidlandsBiomePicker(SimplexClimate climate) {
		return midlandBiomes.get(climate);
	}
	public static BiomePicker getHighlandsBiomePicker(SimplexClimate climate) {
		return highlandBiomes.get(climate);
	}
	public static BiomePicker getMountainPeaksBiomePicker(SimplexClimate climate) {
		return mountainPeaksBiomes.get(climate);
	}

	public static Map<Biome, Pair<Biome, Integer>> getReplacementBiomes() {
		return replacementBiomes;
	}
}
