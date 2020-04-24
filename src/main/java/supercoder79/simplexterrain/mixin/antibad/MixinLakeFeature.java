package supercoder79.simplexterrain.mixin.antibad;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import net.minecraft.world.gen.feature.LakeFeature;
import net.minecraft.world.gen.feature.SingleStateFeatureConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import supercoder79.simplexterrain.SimplexTerrain;

import java.util.Random;

@Mixin(LakeFeature.class)
public class MixinLakeFeature {
    @Inject(method = "generate", at = @At("HEAD"), cancellable = true)
    public void noLakesPls(IWorld iWorld, StructureAccessor accessor, ChunkGenerator<? extends ChunkGeneratorConfig> chunkGenerator, Random random, BlockPos blockPos, SingleStateFeatureConfig singleStateFeatureConfig, CallbackInfoReturnable<Boolean> info) {
        if (SimplexTerrain.CONFIG.deleteLakes) info.setReturnValue(false);
    }
}
