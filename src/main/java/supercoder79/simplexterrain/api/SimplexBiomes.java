package supercoder79.simplexterrain.api;

import net.minecraft.util.Identifier;
import supercoder79.simplexterrain.impl.SimplexBiomesImpl;

/**
 * API front for Simplex Biomes generator
 *
 * @author Valoeghese and Supercoder79
 */
public final class SimplexBiomes {
	private SimplexBiomes() {
	}
	
	public static void addLowlandsBiome(Identifier biome, SimplexClimate climate, double weight) {
		SimplexBiomesImpl.addToLowlands(biome, climate, weight);
	}
	
	public static void addMidlandsBiome(Identifier biome, SimplexClimate climate, double weight) {
		SimplexBiomesImpl.addToMidlands(biome, climate, weight);
	}
	
	public void addHighlandsBiome(Identifier biome, SimplexClimate climate, double weight) {
		SimplexBiomesImpl.addToHighlands(biome, climate, weight);
	}
	
	public void addMountainPeaksBiome(Identifier biome, SimplexClimate climate, double weight) {
		SimplexBiomesImpl.addToMountainPeaks(biome, climate, weight);
	}
}
