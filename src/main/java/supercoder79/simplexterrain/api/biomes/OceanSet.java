package supercoder79.simplexterrain.api.biomes;

import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

public enum OceanSet {
	TROPICAL(BiomeKeys.BEACH.getValue(), BiomeKeys.WARM_OCEAN.getValue(), BiomeKeys.DEEP_WARM_OCEAN.getValue()),
	TEMPERATE(BiomeKeys.BEACH.getValue(), BiomeKeys.OCEAN.getValue(), BiomeKeys.DEEP_OCEAN.getValue()),
	COLD(BiomeKeys.BEACH.getValue(), BiomeKeys.COLD_OCEAN.getValue(), BiomeKeys.DEEP_COLD_OCEAN.getValue()),
	FROZEN(BiomeKeys.SNOWY_BEACH.getValue(), BiomeKeys.FROZEN_OCEAN.getValue(), BiomeKeys.DEEP_FROZEN_OCEAN.getValue());

	public final Identifier shore, ocean, deepOcean;

	OceanSet(Identifier shore, Identifier ocean, Identifier deepOcean) {
		this.shore = shore;
		this.ocean = ocean;
		this.deepOcean = deepOcean;
	}
	
}
