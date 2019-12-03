package supercoder79.simplexterrain.world.biomelayers;

import java.util.function.LongFunction;

import net.minecraft.world.biome.layer.ScaleLayer;
import net.minecraft.world.biome.layer.SmoothenShorelineLayer;
import net.minecraft.world.biome.layer.type.ParentedLayer;
import net.minecraft.world.biome.layer.util.CachingLayerContext;
import net.minecraft.world.biome.layer.util.CachingLayerSampler;
import net.minecraft.world.biome.layer.util.LayerFactory;
import net.minecraft.world.biome.layer.util.LayerSampleContext;
import net.minecraft.world.biome.layer.util.LayerSampler;
import net.minecraft.world.biome.source.BiomeLayerSampler;
import net.minecraft.world.level.LevelGeneratorType;
import supercoder79.simplexterrain.SimplexTerrain;
import supercoder79.simplexterrain.world.biomelayers.layers.AddSpecialForestsLayer;
import supercoder79.simplexterrain.world.biomelayers.layers.ForestLayer;
import supercoder79.simplexterrain.world.biomelayers.layers.HighlandsBiomePassLayer;
import supercoder79.simplexterrain.world.biomelayers.layers.LowlandsBiomePassLayer;
import supercoder79.simplexterrain.world.biomelayers.layers.MidlandsBiomePassLayer;
import supercoder79.simplexterrain.world.biomelayers.layers.MountainsLayer;
import supercoder79.simplexterrain.world.biomelayers.layers.PutBiomesOutOfTheirMiseryLayer;
import supercoder79.simplexterrain.world.biomelayers.layers.SimplexClimateLayer;
import supercoder79.simplexterrain.world.biomelayers.layers.TaigaLayer;
import supercoder79.simplexterrain.world.biomelayers.layers.ToplandsBiomePassLayer;

public class LandBiomeLayers {
	private static <T extends LayerSampler, C extends LayerSampleContext<T>> LayerFactory<T> repeat(long l, ParentedLayer parentedLayer, LayerFactory<T> layerFactory, int i, LongFunction<C> longFunction) {
		LayerFactory<T> result = layerFactory;

		for(int j = 0; j < i; ++j) {
			result = parentedLayer.create(longFunction.apply(l + (long)j), result);
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	public static <T extends LayerSampler, C extends LayerSampleContext<T>> LayerFactory<T>[] stackFactories(LevelGeneratorType levelGeneratorType, long worldSeed, LongFunction<C> contextProvider) {
		LayerFactory<T> climateLayer = new SimplexClimateLayer(worldSeed).create(contextProvider.apply(1L));

		//lowlands (y67 - y90)
		LayerFactory<T> lowlandsBiomeLayer = climateLayer;
		for (int i = 0; i < SimplexTerrain.CONFIG.lowlandBiomeAdditionAttempts; i++) {
			lowlandsBiomeLayer = LowlandsBiomePassLayer.INSTANCE.create(contextProvider.apply( (1000*i)+i), lowlandsBiomeLayer);
		}
		lowlandsBiomeLayer = repeat(1000L, ScaleLayer.NORMAL, lowlandsBiomeLayer, SimplexTerrain.CONFIG.biomeScaleAmount, contextProvider);
		lowlandsBiomeLayer = SmoothenShorelineLayer.INSTANCE.create(contextProvider.apply(20), lowlandsBiomeLayer);

		lowlandsBiomeLayer = PutBiomesOutOfTheirMiseryLayer.INSTANCE.create(contextProvider.apply(1), lowlandsBiomeLayer);



		//midlands (y91-y140)
		LayerFactory<T> midlandsBiomeLayer = ForestLayer.INSTANCE.create(contextProvider.apply(1L));
		for (int i = 0; i < SimplexTerrain.CONFIG.midlandBiomeAdditionAttempts; i++) {
			midlandsBiomeLayer = MidlandsBiomePassLayer.INSTANCE.create(contextProvider.apply( (1000*i)+i), midlandsBiomeLayer);
		}
		midlandsBiomeLayer = AddSpecialForestsLayer.INSTANCE.create(contextProvider.apply(2000), midlandsBiomeLayer);
		midlandsBiomeLayer = repeat(1000L, ScaleLayer.NORMAL, midlandsBiomeLayer, SimplexTerrain.CONFIG.biomeScaleAmount, contextProvider);
		midlandsBiomeLayer = SmoothenShorelineLayer.INSTANCE.create(contextProvider.apply(20), midlandsBiomeLayer);

		midlandsBiomeLayer = PutBiomesOutOfTheirMiseryLayer.INSTANCE.create(contextProvider.apply(1), midlandsBiomeLayer);



		//highlands (y141-y190)
		LayerFactory<T> highlandsBiomeLayer = TaigaLayer.INSTANCE.create(contextProvider.apply(1L));
		for (int i = 0; i < SimplexTerrain.CONFIG.highlandBiomeAdditionAttempts; i++) {
			highlandsBiomeLayer = HighlandsBiomePassLayer.INSTANCE.create(contextProvider.apply( (1000*i)+i), highlandsBiomeLayer);
		}
		highlandsBiomeLayer = repeat(1000L, ScaleLayer.NORMAL, highlandsBiomeLayer, SimplexTerrain.CONFIG.biomeScaleAmount, contextProvider);
		highlandsBiomeLayer = SmoothenShorelineLayer.INSTANCE.create(contextProvider.apply(20), highlandsBiomeLayer);

		highlandsBiomeLayer = PutBiomesOutOfTheirMiseryLayer.INSTANCE.create(contextProvider.apply(1), highlandsBiomeLayer);



		//toplands (y191+)
		LayerFactory<T> toplandsBiomeLayer = MountainsLayer.INSTANCE.create(contextProvider.apply(1L));
		for (int i = 0; i < SimplexTerrain.CONFIG.toplandBiomeAdditionAttempts; i++) {
			toplandsBiomeLayer = ToplandsBiomePassLayer.INSTANCE.create(contextProvider.apply( (1000*i)+i), toplandsBiomeLayer);
		}
		toplandsBiomeLayer = repeat(1000L, ScaleLayer.NORMAL, toplandsBiomeLayer, SimplexTerrain.CONFIG.biomeScaleAmount, contextProvider);
		toplandsBiomeLayer = SmoothenShorelineLayer.INSTANCE.create(contextProvider.apply(20), toplandsBiomeLayer);

		toplandsBiomeLayer = PutBiomesOutOfTheirMiseryLayer.INSTANCE.create(contextProvider.apply(1), toplandsBiomeLayer);

		return new LayerFactory[]{lowlandsBiomeLayer, midlandsBiomeLayer, highlandsBiomeLayer, toplandsBiomeLayer};
	}

	public static BiomeLayerSampler[] build(long l, LevelGeneratorType levelGeneratorType) {
		LayerFactory<CachingLayerSampler>[] layerFactory = stackFactories(levelGeneratorType, l, (m) -> new CachingLayerContext(25, l, m));
		return new BiomeLayerSampler[]{new BiomeLayerSampler(layerFactory[0]), new BiomeLayerSampler(layerFactory[1]), new BiomeLayerSampler(layerFactory[2]), new BiomeLayerSampler(layerFactory[3])};
	}
}
