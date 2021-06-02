package supercoder79.simplexterrain.world.gen;

import it.unimi.dsi.fastutil.HashCommon;
import net.minecraft.util.math.ChunkPos;
import supercoder79.simplexterrain.api.Heightmap;
import supercoder79.simplexterrain.noise.gradient.OpenSimplexNoise;

import java.util.Arrays;

public class ContinentCache {
    private final long[] keys;
    private final int[] values;

    private final OpenSimplexNoise noise;
    private final int seaLevel;

    public ContinentCache(long seed, int seaLevel) {
        this.noise = new OpenSimplexNoise(seed);
        this.seaLevel = seaLevel;

        this.keys = new long[2048];
        this.values = new int[2048];

        Arrays.fill(this.keys, Long.MIN_VALUE);
    }

    public int get(int x, int z) {
        long key = key(x, z);
        int idx = hash(key) & 2047;

        if (this.keys[idx] == key) {
            return this.values[idx];
        }

        int height = (int) (this.seaLevel + (noise.sample(x / 1200.0, z / 1200.0) + 0.2) * 16);
        this.values[idx] = height;
        this.keys[idx] = key;

        return height;
    }

    private static int hash(long key) {
        return (int) HashCommon.mix(key);
    }

    private static long key(int x, int z) {
        return ChunkPos.toLong(x, z);
    }
}
