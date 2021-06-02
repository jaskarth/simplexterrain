package supercoder79.simplexterrain.world.gen;

import supercoder79.simplexterrain.api.Heightmap;

public class ContinentGenerator implements Heightmap {
    private final ThreadLocal<ContinentCache> cache;

    public ContinentGenerator(long seed, int seaLevel) {
        this.cache = ThreadLocal.withInitial(() -> new ContinentCache(seed, seaLevel));
    }

    @Override
    public int getHeight(int x, int z) {
        return this.cache.get().get(x, z);
    }
}
