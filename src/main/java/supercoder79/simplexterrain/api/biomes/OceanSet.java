package supercoder79.simplexterrain.api.biomes;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;

/**
 * Maps temperature to an ocean type, deep ocean type, and beach type.
 *
 * @author Valoeghese
 */
public enum OceanSet {
	TROPICAL(Biomes.BEACH, Biomes.WARM_OCEAN, Biomes.DEEP_WARM_OCEAN),
	TEMPERATE(Biomes.BEACH, Biomes.OCEAN, Biomes.DEEP_OCEAN),
	COLD(Biomes.BEACH, Biomes.COLD_OCEAN, Biomes.DEEP_COLD_OCEAN),
	FROZEN(Biomes.SNOWY_BEACH, Biomes.FROZEN_OCEAN, Biomes.DEEP_FROZEN_OCEAN);

	OceanSet(Biome shore, Biome ocean, Biome deepOcean) {
		this.shore = shore;
		this.ocean = ocean;
		this.deepOcean = deepOcean;
	}
	
	public final Biome shore, ocean, deepOcean;
}
