package supercoder79.simplexterrain.world.biomelayers;

import java.util.function.LongFunction;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.layer.ScaleLayer;
import net.minecraft.world.biome.layer.SmoothLayer;
import net.minecraft.world.biome.layer.type.ParentedLayer;
import net.minecraft.world.biome.layer.util.CachingLayerContext;
import net.minecraft.world.biome.layer.util.CachingLayerSampler;
import net.minecraft.world.biome.layer.util.LayerFactory;
import net.minecraft.world.biome.layer.util.LayerSampleContext;
import net.minecraft.world.biome.layer.util.LayerSampler;
import net.minecraft.world.biome.source.BiomeLayerSampler;
import supercoder79.simplexterrain.SimplexTerrain;
import supercoder79.simplexterrain.world.biomelayers.layers.*;

public class SimplexBiomeLayers {
	private static <T extends LayerSampler, C extends LayerSampleContext<T>> LayerFactory<T> repeat(long l, ParentedLayer parentedLayer, LayerFactory<T> layerFactory, int i, LongFunction<C> longFunction) {
		LayerFactory<T> result = layerFactory;

		for(int j = 0; j < i; ++j) {
			result = parentedLayer.create(longFunction.apply(l + (long)j), result);
		}

		return result;
	}

	private static <T extends LayerSampler, C extends LayerSampleContext<T>> LayerFactory<T>[] stackFactories(Registry<Biome> biomes, long worldSeed, LongFunction<C> contextProvider) {
		LayerFactory<T> climateLayer = new SimplexClimateLayer(worldSeed).create(contextProvider.apply(1L));

		//lowlands (y67 - y90)
		LayerFactory<T> lowlandsBiomeLayer = new LowlandsBiomeLayer(biomes).create(contextProvider.apply(100L), climateLayer);
		lowlandsBiomeLayer = ReplaceBiomesLayer.INSTANCE.create(contextProvider.apply(2000), lowlandsBiomeLayer);
		lowlandsBiomeLayer = repeat(1000L, ScaleLayer.NORMAL, lowlandsBiomeLayer, SimplexTerrain.CONFIG.biomeScaleAmount, contextProvider);
		lowlandsBiomeLayer = SmoothLayer.INSTANCE.create(contextProvider.apply(20), lowlandsBiomeLayer);
		lowlandsBiomeLayer = SmoothLayer.INSTANCE.create(contextProvider.apply(25), lowlandsBiomeLayer);

		//midlands (y91-y140)
		LayerFactory<T> midlandsBiomeLayer = new MidlandsBiomeLayer(biomes).create(contextProvider.apply(100L), climateLayer);
		midlandsBiomeLayer = ReplaceBiomesLayer.INSTANCE.create(contextProvider.apply(2000), midlandsBiomeLayer);
		midlandsBiomeLayer = repeat(1000L, ScaleLayer.NORMAL, midlandsBiomeLayer, SimplexTerrain.CONFIG.biomeScaleAmount, contextProvider);
		midlandsBiomeLayer = SmoothLayer.INSTANCE.create(contextProvider.apply(20), midlandsBiomeLayer);
		midlandsBiomeLayer = SmoothLayer.INSTANCE.create(contextProvider.apply(25), midlandsBiomeLayer);

		//highlands (y141-y190)
		LayerFactory<T> highlandsBiomeLayer = new HighlandsBiomeLayer(biomes).create(contextProvider.apply(100L), climateLayer);
		highlandsBiomeLayer = ReplaceBiomesLayer.INSTANCE.create(contextProvider.apply(2000), highlandsBiomeLayer);
		highlandsBiomeLayer = repeat(1000L, ScaleLayer.NORMAL, highlandsBiomeLayer, SimplexTerrain.CONFIG.biomeScaleAmount, contextProvider);
		highlandsBiomeLayer = SmoothLayer.INSTANCE.create(contextProvider.apply(20), highlandsBiomeLayer);
		highlandsBiomeLayer = SmoothLayer.INSTANCE.create(contextProvider.apply(25), highlandsBiomeLayer);

		//mountain peaks (y191+)
		LayerFactory<T> mountainPeaksBiomePassLayer = new MountainPeaksBiomeLayer(biomes).create(contextProvider.apply(100L), climateLayer);
		mountainPeaksBiomePassLayer = ReplaceBiomesLayer.INSTANCE.create(contextProvider.apply(2000), mountainPeaksBiomePassLayer);
		mountainPeaksBiomePassLayer = repeat(1000L, ScaleLayer.NORMAL, mountainPeaksBiomePassLayer, SimplexTerrain.CONFIG.biomeScaleAmount, contextProvider);
		mountainPeaksBiomePassLayer = SmoothLayer.INSTANCE.create(contextProvider.apply(20), mountainPeaksBiomePassLayer);
		mountainPeaksBiomePassLayer = SmoothLayer.INSTANCE.create(contextProvider.apply(25), mountainPeaksBiomePassLayer);
		
		LayerFactory<T> shoreSampler = ClimateTransformerLayer.shore(biomes).create(contextProvider.apply(0), climateLayer);
		shoreSampler = repeat(1000L, ScaleLayer.NORMAL, shoreSampler, SimplexTerrain.CONFIG.biomeScaleAmount, contextProvider);
		
		LayerFactory<T> oceanSampler = ClimateTransformerLayer.ocean(biomes).create(contextProvider.apply(0), climateLayer);
		oceanSampler = repeat(1000L, ScaleLayer.NORMAL, oceanSampler, SimplexTerrain.CONFIG.biomeScaleAmount, contextProvider);

		LayerFactory<T> deepOceanSampler = ClimateTransformerLayer.deepOcean(biomes).create(contextProvider.apply(0), climateLayer);
		deepOceanSampler = repeat(1000L, ScaleLayer.NORMAL, deepOceanSampler, SimplexTerrain.CONFIG.biomeScaleAmount, contextProvider);

		return new LayerFactory[]{lowlandsBiomeLayer, midlandsBiomeLayer, highlandsBiomeLayer, mountainPeaksBiomePassLayer, shoreSampler, oceanSampler, deepOceanSampler};
	}

	public static BiomeLayerSampler[] build(Registry<Biome> biomes, long l) {
		LayerFactory<CachingLayerSampler>[] arr = stackFactories(biomes, l, (salt) -> new CachingLayerContext(5, l, salt));
		return new BiomeLayerSampler[]{
				new BiomeLayerSampler(arr[0]),
				new BiomeLayerSampler(arr[1]),
				new BiomeLayerSampler(arr[2]),
				new BiomeLayerSampler(arr[3]),
				new BiomeLayerSampler(arr[4]),
				new BiomeLayerSampler(arr[5]),
				new BiomeLayerSampler(arr[6])};
	}
}
