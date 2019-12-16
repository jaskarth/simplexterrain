package supercoder79.simplexterrain.world.biome;

import net.minecraft.util.Identifier;
import supercoder79.simplexterrain.api.biomes.SimplexClimate;

import java.util.HashMap;
import java.util.Map;

public final class SimplexBiomesImpl {
	private SimplexBiomesImpl() {
	}

	private static final Map<SimplexClimate, BiomePicker> lowlandBiomes = new HashMap<>();
	private static final Map<SimplexClimate, BiomePicker> midlandBiomes = new HashMap<>();
	private static final Map<SimplexClimate, BiomePicker> highlandBiomes = new HashMap<>();
	private static final Map<SimplexClimate, BiomePicker> mountainPeaksBiomes = new HashMap<>();

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
}
