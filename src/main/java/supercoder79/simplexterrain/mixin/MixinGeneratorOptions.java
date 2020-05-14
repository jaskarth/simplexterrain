package supercoder79.simplexterrain.mixin;

import com.mojang.datafixers.Dynamic;
import net.minecraft.class_5285;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_5285.class)
public class MixinGeneratorOptions {

    @Inject(method = "method_28013", at = @At("HEAD"), cancellable = true)
    private static void hookMakeChunkGenerator(class_5285.class_5287 arg, Dynamic<?> dynamic, long l, CallbackInfoReturnable<ChunkGenerator> cir) {

    }
}
