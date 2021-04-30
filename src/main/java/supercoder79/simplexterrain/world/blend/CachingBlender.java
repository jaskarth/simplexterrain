package supercoder79.simplexterrain.world.blend;

import it.unimi.dsi.fastutil.HashCommon;
import net.minecraft.util.math.ChunkPos;

import java.util.Arrays;

public final class CachingBlender {
    private final long[] keys;
    private final LinkedBiomeWeightMap[] values;
    private final ScatteredBiomeBlender internal;

    public CachingBlender(double samplingFrequency, double blendRadiusPadding, int chunkWidth) {
        this.internal = new ScatteredBiomeBlender(samplingFrequency, blendRadiusPadding, chunkWidth);

        this.keys = new long[512];
        this.values = new LinkedBiomeWeightMap[512];

        Arrays.fill(this.keys, Long.MIN_VALUE);
    }

    public LinkedBiomeWeightMap getBlendForChunk(long seed, int chunkBaseWorldX, int chunkBaseWorldZ, ScatteredBiomeBlender.BiomeEvaluationCallback callback) {
        long key = key(chunkBaseWorldX, chunkBaseWorldZ);
        int idx = hash(key) & 511;

        if (this.keys[idx] == key) {
            return this.values[idx];
        }

        LinkedBiomeWeightMap weightMap = this.internal.getBlendForChunk(seed, chunkBaseWorldX, chunkBaseWorldZ, callback);
        this.values[idx] = weightMap;
        this.keys[idx] = key;

        return weightMap;
    }

    private static int hash(long key) {
        return (int) HashCommon.mix(key);
    }

    private static long key(int x, int z) {
        return ChunkPos.toLong(x, z);
    }
}
