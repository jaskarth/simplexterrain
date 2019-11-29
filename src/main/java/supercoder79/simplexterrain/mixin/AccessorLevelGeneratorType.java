package supercoder79.simplexterrain.mixin;

import net.minecraft.world.level.LevelGeneratorType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelGeneratorType.class)
public interface AccessorLevelGeneratorType {
    @Invoker("<init>")
    static LevelGeneratorType create(int id, String name) {
        throw new RuntimeException("f");
    }
}