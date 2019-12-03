package supercoder79.simplexterrain.impl;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.util.Identifier;
import supercoder79.simplexterrain.api.SimplexClimate;

public final class SimplexBiomesImpl {
	private SimplexBiomesImpl() {
	}

	public static final Map<SimplexClimate, BiomePicker> lowlandBiomes = new HashMap<>();
	public static final Map<SimplexClimate, BiomePicker> midlandBiomes = new HashMap<>();
	public static final Map<SimplexClimate, BiomePicker> highlandBiomes = new HashMap<>();
	public static final Map<SimplexClimate, BiomePicker> toplandBiomes = new HashMap<>();

	public static void addToLowlands(Identifier biome, SimplexClimate climate, double weight) {
		lowlandBiomes.computeIfAbsent(climate, simplexClimate -> new BiomePicker()).addBiome(biome, weight);
	}
	public static void addToMidlands(Identifier biome, SimplexClimate climate, double weight) {
		midlandBiomes.computeIfAbsent(climate, simplexClimate -> new BiomePicker()).addBiome(biome, weight);
	}
	public static void addToHighlands(Identifier biome, SimplexClimate climate, double weight) {
		highlandBiomes.computeIfAbsent(climate, simplexClimate -> new BiomePicker()).addBiome(biome, weight);
	}
	public static void addToToplands(Identifier biome, SimplexClimate climate, double weight) {
		toplandBiomes.computeIfAbsent(climate, simplexClimate -> new BiomePicker()).addBiome(biome, weight);
	}
}
