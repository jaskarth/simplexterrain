package supercoder79.simplexterrain.mixin;

import net.minecraft.class_5284;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import supercoder79.simplexterrain.SimplexTerrain;
import supercoder79.simplexterrain.world.gen.SimplexNetherGeneration;

@Mixin(SurfaceChunkGenerator.class)
public abstract class MixinCavesChunkGenerator extends ChunkGenerator {

    public MixinCavesChunkGenerator(BiomeSource biomeSource, ChunkGeneratorConfig config) {
        super(biomeSource, config);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void constructor(BiomeSource biomeSource, long seed, class_5284 config, int horizontalNoiseResolution, int verticalNoiseResolution, int worldHeight, boolean useSimplexNoise, CallbackInfo ci) {
        SimplexNetherGeneration.init(seed);
    }

    @Inject(method = "populateNoise", at = @At("HEAD"), cancellable = true)
    public void populateNoise(WorldAccess world, StructureAccessor accessor, Chunk chunk, CallbackInfo ci) {
        if (SimplexTerrain.isSimplexEnabled && (ChunkGenerator)this instanceof CavesChunkGenerator && world.getDimension().getType() == DimensionType.THE_NETHER) {
            System.out.println("");
            SimplexNetherGeneration.generate(world, chunk, this.biomeSource, getSeaLevel());
            ci.cancel();
        }
    }
}
