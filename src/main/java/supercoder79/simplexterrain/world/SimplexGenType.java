package supercoder79.simplexterrain.world;

import net.minecraft.class_5317;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import supercoder79.simplexterrain.world.gen.SimplexBiomeSource;
import supercoder79.simplexterrain.world.gen.SimplexChunkGenerator;

public class SimplexGenType extends class_5317 {
    public SimplexGenType() {
        super("simplex");
        class_5317.field_25052.add(this);
    }

    @Override
    protected ChunkGenerator method_29076(long l) {
        return new SimplexChunkGenerator(new SimplexBiomeSource(l), l);
    }
}
