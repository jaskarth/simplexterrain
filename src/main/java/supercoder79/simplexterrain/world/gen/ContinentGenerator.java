package supercoder79.simplexterrain.world.gen;

import supercoder79.simplexterrain.api.Heightmap;
import supercoder79.simplexterrain.noise.gradient.OpenSimplexNoise;

public class ContinentGenerator implements Heightmap {
    private final OpenSimplexNoise noise;
    private final int seaLevel;

    public ContinentGenerator(long seed, int seaLevel) {
        this.noise = new OpenSimplexNoise(seed);
        this.seaLevel = seaLevel;
    }


    @Override
    public int getHeight(int x, int z) {
        return (int) (this.seaLevel + (noise.sample(x / 1200.0, z / 1200.0) + 0.2) * 16);
    }
}
