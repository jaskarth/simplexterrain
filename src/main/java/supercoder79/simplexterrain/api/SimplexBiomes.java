package supercoder79.simplexterrain.api;

import net.minecraft.util.Identifier;
import supercoder79.simplexterrain.impl.SimplexBiomesImpl;

/**
 * API front for Simplex Biomes generator
 *
 * @author Valoeghese
 */
public final class SimplexBiomes {
	private SimplexBiomes() {
	}
	
	public void addLowlandsBiome(Identifier biome, SimplexClimate climate, double weight) {
		SimplexBiomesImpl.addToLowlands(biome, climate, weight);
	}
	
	public void addMidlandsBiome(Identifier biome, SimplexClimate climate, double weight) {
		SimplexBiomesImpl.addToMidlands(biome, climate, weight);
	}
	
	public void addHighlandsBiome(Identifier biome, SimplexClimate climate, double weight) {
		SimplexBiomesImpl.addToHighlands(biome, climate, weight);
	}
	
	public void addMountainPeaksBiome(Identifier biome, SimplexClimate climate, double weight) {
		SimplexBiomesImpl.addToToplands(biome, climate, weight);
	}
}
