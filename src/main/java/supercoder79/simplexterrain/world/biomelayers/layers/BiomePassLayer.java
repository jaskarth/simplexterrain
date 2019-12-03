package supercoder79.simplexterrain.world.biomelayers.layers;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.layer.type.SouthEastSamplingLayer;

interface BiomePassLayer extends SouthEastSamplingLayer {
	int PLAINS = Registry.BIOME.getRawId(Biomes.PLAINS);
	int FOREST = Registry.BIOME.getRawId(Biomes.FOREST);
	int TAIGA = Registry.BIOME.getRawId(Biomes.TAIGA);
	int MOUNTAINS = Registry.BIOME.getRawId(Biomes.MOUNTAINS);
}
