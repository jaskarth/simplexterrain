package supercoder79.simplexterrain.terrain.biomelayers.layers;

import net.minecraft.world.biome.layer.util.LayerRandomnessSource;
import supercoder79.simplexterrain.api.SquareCrossSamplingLayer;

public enum PutBiomesOutOfTheirMiseryLayer implements SquareCrossSamplingLayer {
    INSTANCE;

    @Override
    public int sampleSquare(LayerRandomnessSource layerRandomnessSource, int w, int nw, int n, int ne, int e, int se, int s, int sw, int center) {
//        int similarity = 0;
//        if (center == w) similarity++;
//        if (center == nw) similarity++;
//        if (center == n) similarity++;
//        if (center == ne) similarity++;
//        if (center == e) similarity++;
//        if (center == se) similarity++;
//        if (center == s) similarity++;
//        if (center == se) similarity++;
//        if (similarity <= 4) {
//            System.out.println("putting a biome out of it's misery");
//            return n;
//        }

        //TODO: actually add this

        return center;
    }
}
