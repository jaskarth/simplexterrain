package supercoder79.simplexterrain.world.noisetype;

import net.minecraft.world.gen.ChunkRandom;
import supercoder79.simplexterrain.world.BiomeData;

public class DefaultNoiseType implements NoiseType {
    public static final DefaultNoiseType INSTANCE = new DefaultNoiseType();

    @Override
    public void init(ChunkRandom random) {

    }

    @Override
    public double modify(int x, int z, double currentNoiseValue, double weight, BiomeData data) {
        return 0;
    }
}
