package supercoder79.simplexterrain.mixin;

import net.minecraft.world.level.LevelGeneratorType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LevelGeneratorType.class)
public interface AccessorLevelGeneratorType {
    @Invoker("<init>")
    static LevelGeneratorType create(int id, String name) {
        throw new RuntimeException("f");
    }
}