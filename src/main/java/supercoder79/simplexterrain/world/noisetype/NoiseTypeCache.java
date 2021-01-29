package supercoder79.simplexterrain.world.noisetype;

import it.unimi.dsi.fastutil.HashCommon;
import net.minecraft.util.math.ChunkPos;

import java.util.Arrays;

public class NoiseTypeCache {
    private final NoiseTypePicker picker;
    private final ThreadLocal<Cache> cache;

    public NoiseTypeCache(NoiseTypePicker picker) {
        this.picker = picker;
        this.cache = ThreadLocal.withInitial(() -> new Cache(this.picker));
    }

    public NoiseType get(int x, int z) {
        return this.cache.get().get(x, z);
    }

    private static class Cache {
        private final long[] keys;
        private final NoiseType[] values;
        private final NoiseTypePicker picker;

        private Cache(NoiseTypePicker picker) {
            this.picker = picker;
            this.keys = new long[2048];
            this.values = new NoiseType[2048];

            Arrays.fill(this.keys, Long.MIN_VALUE);
        }

        public NoiseType get(int x, int z) {
            long key = key(x, z);
            int idx = hash(key) & 2047;

            if (this.keys[idx] == key) {
                return this.values[idx];
            }

            NoiseType type = this.picker.get(x, z);
            this.values[idx] = type;
            this.keys[idx] = key;

            return type;
        }

        private static int hash(long key) {
            return (int) HashCommon.mix(key);
        }

        private static long key(int x, int z) {
            return ChunkPos.toLong(x, z);
        }
    }
}
