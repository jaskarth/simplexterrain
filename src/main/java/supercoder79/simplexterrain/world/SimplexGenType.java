package supercoder79.simplexterrain.world;

import net.minecraft.client.world.GeneratorType;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import supercoder79.simplexterrain.world.gen.SimplexBiomeSource;
import supercoder79.simplexterrain.world.gen.SimplexChunkGenerator;

public class SimplexGenType extends GeneratorType {
    public SimplexGenType() {
        super("simplex");
        GeneratorType.VALUES.add(this);
    }

    @Override
    protected ChunkGenerator method_29076(long l) {
        return new SimplexChunkGenerator(new SimplexBiomeSource(l), l);
    }
}
