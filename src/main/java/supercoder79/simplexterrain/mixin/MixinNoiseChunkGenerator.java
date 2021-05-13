package supercoder79.simplexterrain.mixin;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.Biome;
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

import java.util.function.Supplier;

@Mixin(NoiseChunkGenerator.class)
public abstract class MixinNoiseChunkGenerator extends ChunkGenerator {

    public MixinNoiseChunkGenerator(BiomeSource biomeSource, StructuresConfig config) {
        super(biomeSource, config);
    }

    @Inject(method = "<init>(Lnet/minecraft/world/biome/source/BiomeSource;Lnet/minecraft/world/biome/source/BiomeSource;JLjava/util/function/Supplier;)V", at = @At("RETURN"))
    private void constructor(BiomeSource biomeSource, BiomeSource biomeSource2, long worldSeed, Supplier<ChunkGeneratorSettings> supplier, CallbackInfo ci) {
        SimplexNetherGeneration.init(worldSeed);
    }

    @Inject(method = "populateNoise", at = @At("HEAD"), cancellable = true)
    public void populateNoise(WorldAccess world, StructureAccessor accessor, Chunk chunk, CallbackInfo ci) {
        Registry<DimensionType> dimensions = ((ChunkRegion)world).toServerWorld().getRegistryManager().get(Registry.DIMENSION_TYPE_KEY);

        // replace the nether with ours if we're in a simplex terrain world and nether generation is enabled
        if (SimplexTerrain.isSimplexEnabled && dimensions.getId(world.getDimension()) == DimensionType.THE_NETHER_REGISTRY_KEY.getValue() && SimplexTerrain.CONFIG.simplexNetherGeneration) {
            SimplexNetherGeneration.generate(world, chunk, this.biomeSource, getSeaLevel());
            ci.cancel();
        }
    }
}
