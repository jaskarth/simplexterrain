package supercoder79.simplexterrain.mixin;

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

    public MixinCavesChunkGenerator(BiomeSource biomeSource, StructuresConfig config) {
        super(biomeSource, config);
    }

    @Inject(method = "<init>(Lnet/minecraft/world/biome/source/BiomeSource;Lnet/minecraft/world/biome/source/BiomeSource;JLnet/minecraft/world/gen/chunk/ChunkGeneratorType;)V", at = @At("RETURN"))
    private void constructor(BiomeSource biomeSource, BiomeSource biomeSource2, long l, ChunkGeneratorType chunkGeneratorType, CallbackInfo ci) {
        SimplexNetherGeneration.init(l);
    }

    @Inject(method = "populateNoise", at = @At("HEAD"), cancellable = true)
    public void populateNoise(WorldAccess world, StructureAccessor accessor, Chunk chunk, CallbackInfo ci) {
        if (SimplexTerrain.isSimplexEnabled && world.getWorld().getDimensionRegistryKey() == DimensionType.THE_NETHER_REGISTRY_KEY) {
            SimplexNetherGeneration.generate(world, chunk, this.biomeSource, getSeaLevel());
            ci.cancel();
        }
    }
}
