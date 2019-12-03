package supercoder79.simplexterrain.world.biomelayers.layers;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.layer.type.SouthEastSamplingLayer;

interface BiomePassLayer extends SouthEastSamplingLayer {
	static final int PLAINS = Registry.BIOME.getRawId(Biomes.PLAINS);
	static final int FOREST = Registry.BIOME.getRawId(Biomes.FOREST);
	static final int TAIGA = Registry.BIOME.getRawId(Biomes.TAIGA);
	static final int MOUNTAINS = Registry.BIOME.getRawId(Biomes.MOUNTAINS);
}
