package supercoder79.simplexterrain.terrain.biomelayers;

import net.minecraft.world.biome.layer.*;
import net.minecraft.world.biome.layer.type.ParentedLayer;
import net.minecraft.world.biome.layer.util.*;
import net.minecraft.world.biome.source.BiomeLayerSampler;
import net.minecraft.world.gen.chunk.OverworldChunkGeneratorConfig;
import net.minecraft.world.level.LevelGeneratorType;
import supercoder79.simplexterrain.terrain.biomelayers.layers.*;

import java.util.function.LongFunction;

public class LandBiomeLayers {
    private static <T extends LayerSampler, C extends LayerSampleContext<T>> LayerFactory<T> stack(long l, ParentedLayer parentedLayer, LayerFactory<T> layerFactory, int i, LongFunction<C> longFunction) {
        LayerFactory<T> layerFactory2 = layerFactory;

        for(int j = 0; j < i; ++j) {
            layerFactory2 = parentedLayer.create(longFunction.apply(l + (long)j), layerFactory2);
        }

        return layerFactory2;
    }

    public static <T extends LayerSampler, C extends LayerSampleContext<T>> LayerFactory<T>[] build(LevelGeneratorType levelGeneratorType, OverworldChunkGeneratorConfig overworldChunkGeneratorConfig, LongFunction<C> longFunction) {
        //lowlands (y67 - y90)
        LayerFactory<T> lowlandsBiomeLayer = PlainsLayer.INSTANCE.create(longFunction.apply(1L));
        for (int i = 0; i < 3; i++) {
            lowlandsBiomeLayer = LowlandsBiomePassLayer.INSTANCE.create(longFunction.apply( (1000*i)+i), lowlandsBiomeLayer);
        }
        for(int k = 0; k < 7; ++k) {
            lowlandsBiomeLayer = ScaleLayer.NORMAL.create(longFunction.apply((1000 + k)), lowlandsBiomeLayer);
        }
        lowlandsBiomeLayer = SmoothenShorelineLayer.INSTANCE.create(longFunction.apply(20), lowlandsBiomeLayer);

        lowlandsBiomeLayer = PutBiomesOutOfTheirMiseryLayer.INSTANCE.create(longFunction.apply(1), lowlandsBiomeLayer);



        //midlands (y91-y140)
        LayerFactory<T> midlandsBiomeLayer = ForestLayer.INSTANCE.create(longFunction.apply(1L));
        for (int i = 0; i < 3; i++) {
            midlandsBiomeLayer = MidlandsBiomePassLayer.INSTANCE.create(longFunction.apply( (1000*i)+i), midlandsBiomeLayer);
        }
        midlandsBiomeLayer = AddSpecialForestsLayer.INSTANCE.create(longFunction.apply(2000), midlandsBiomeLayer);
        for(int k = 0; k < 7; ++k) {
            midlandsBiomeLayer = ScaleLayer.NORMAL.create(longFunction.apply((1000 + k)), midlandsBiomeLayer);
        }
        midlandsBiomeLayer = SmoothenShorelineLayer.INSTANCE.create(longFunction.apply(20), midlandsBiomeLayer);

        midlandsBiomeLayer = PutBiomesOutOfTheirMiseryLayer.INSTANCE.create(longFunction.apply(1), midlandsBiomeLayer);



        //highlands (y141-y190)
        LayerFactory<T> highlandsBiomeLayer = TaigaLayer.INSTANCE.create(longFunction.apply(1L));
        for (int i = 0; i < 3; i++) {
            highlandsBiomeLayer = HighlandsBiomePassLayer.INSTANCE.create(longFunction.apply( (1000*i)+i), highlandsBiomeLayer);
        }
        for(int k = 0; k < 7; ++k) {
            highlandsBiomeLayer = ScaleLayer.NORMAL.create(longFunction.apply((1000 + k)), highlandsBiomeLayer);
        }
        highlandsBiomeLayer = SmoothenShorelineLayer.INSTANCE.create(longFunction.apply(20), highlandsBiomeLayer);

        highlandsBiomeLayer = PutBiomesOutOfTheirMiseryLayer.INSTANCE.create(longFunction.apply(1), highlandsBiomeLayer);



        //toplands (y191+)
        LayerFactory<T> toplandsBiomeLayer = MountainsLayer.INSTANCE.create(longFunction.apply(1L));
        for (int i = 0; i < 2; i++) {
            toplandsBiomeLayer = ToplandsBiomePassLayer.INSTANCE.create(longFunction.apply( (1000*i)+i), toplandsBiomeLayer);
        }
        for(int k = 0; k < 7; ++k) {
            toplandsBiomeLayer = ScaleLayer.NORMAL.create(longFunction.apply((1000 + k)), toplandsBiomeLayer);
        }
        toplandsBiomeLayer = SmoothenShorelineLayer.INSTANCE.create(longFunction.apply(20), toplandsBiomeLayer);

        toplandsBiomeLayer = PutBiomesOutOfTheirMiseryLayer.INSTANCE.create(longFunction.apply(1), toplandsBiomeLayer);



        return new LayerFactory[]{lowlandsBiomeLayer, midlandsBiomeLayer, highlandsBiomeLayer, toplandsBiomeLayer};
    }

    public static BiomeLayerSampler[] build(long l, LevelGeneratorType levelGeneratorType, OverworldChunkGeneratorConfig overworldChunkGeneratorConfig) {
        LayerFactory<CachingLayerSampler>[] layerFactory = build(levelGeneratorType, overworldChunkGeneratorConfig, (m) -> new CachingLayerContext(25, l, m));
        return new BiomeLayerSampler[]{new BiomeLayerSampler(layerFactory[0]), new BiomeLayerSampler(layerFactory[1]), new BiomeLayerSampler(layerFactory[2]), new BiomeLayerSampler(layerFactory[3])};
    }
}
