package supercoder79.simplexterrain.api;

import net.minecraft.world.biome.layer.type.ParentedLayer;
import net.minecraft.world.biome.layer.util.LayerRandomnessSource;
import net.minecraft.world.biome.layer.util.LayerSampleContext;
import net.minecraft.world.biome.layer.util.LayerSampler;

/**
 * Samples all biomes in a 3x3 grid. No, i'm not calling it KingTransformer, what gave you that idea?
 * This is probably very bad for performance, so use it sparingly.
 *
 * @author SuperCoder79
 */
public interface SquareCrossSamplingLayer extends ParentedLayer {
    int sampleSquare(LayerRandomnessSource layerRandomnessSource, int w, int nw, int n, int ne, int e, int se, int s, int sw, int center);

    default int sample(LayerSampleContext<?> layerSampleContext, LayerSampler layerSampler, int x, int z) {
        return this.sampleSquare(layerSampleContext,
                layerSampler.sample(this.transformX(x - 1), this.transformZ(z + 0)),
                layerSampler.sample(this.transformX(x - 1), this.transformZ(z + 1)),
                layerSampler.sample(this.transformX(x + 0), this.transformZ(z + 1)),
                layerSampler.sample(this.transformX(x + 1), this.transformZ(z + 1)),
                layerSampler.sample(this.transformX(x + 1), this.transformZ(z + 0)),
                layerSampler.sample(this.transformX(x + 1), this.transformZ(z - 1)),
                layerSampler.sample(this.transformX(x + 0), this.transformZ(z - 1)),
                layerSampler.sample(this.transformX(x - 1), this.transformZ(z - 1)),
                layerSampler.sample(this.transformX(x + 0), this.transformZ(z + 0)));
    }

    @Override
    default int transformX(int i) {
        return i;
    }

    @Override
    default int transformZ(int i) {
        return i;
    }
}
