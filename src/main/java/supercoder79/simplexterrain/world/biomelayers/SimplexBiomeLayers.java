package supercoder79.simplexterrain.world.biomelayers;

import java.util.function.LongFunction;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.layer.ScaleLayer;
import net.minecraft.world.biome.layer.type.ParentedLayer;
import net.minecraft.world.biome.layer.util.CachingLayerContext;
import net.minecraft.world.biome.layer.util.LayerFactory;
import net.minecraft.world.biome.layer.util.LayerSampleContext;
import net.minecraft.world.biome.layer.util.LayerSampler;
import net.minecraft.world.biome.source.BiomeLayerSampler;
import supercoder79.simplexterrain.world.biomelayers.layers.*;

public class SimplexBiomeLayers {
	private static <T extends LayerSampler, C extends LayerSampleContext<T>> LayerFactory<T> stack(long l, ParentedLayer parentedLayer, LayerFactory<T> layerFactory, int i, LongFunction<C> longFunction) {
		LayerFactory<T> result = layerFactory;

		for(int j = 0; j < i; ++j) {
			result = parentedLayer.create(longFunction.apply(l + (long)j), result);
		}

		return result;
	}

	private static <T extends LayerSampler, C extends LayerSampleContext<T>> LayerFactory<T> build(Registry<Biome> biomes, long worldSeed, LongFunction<C> contextProvider) {
		LayerFactory<T> layer = new SimplexClimateLayer(worldSeed).create(contextProvider.apply(1L));
		layer = new BaseBiomesLayer(biomes).create(contextProvider.apply(5L), layer);
		layer = stack(1000, ScaleLayer.NORMAL, layer, 7, contextProvider);

		return layer;
	}

	public static BiomeLayerSampler build(Registry<Biome> biomes, long seed) {
		return new BiomeLayerSampler(build(biomes, seed, salt -> new CachingLayerContext(5, seed, salt)));
	}
}
