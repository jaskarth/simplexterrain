package supercoder79.simplexterrain.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.level.LevelGeneratorType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import supercoder79.simplexterrain.world.WorldType;

@Mixin(LevelGeneratorType.class)
public class MixinLevelGeneratorTypeServer {
	@Shadow @Final private String name;

	@Inject(at = @At("HEAD"), method = "getTypeFromName", cancellable = true)
	private static void getTypeFromName(String name, CallbackInfoReturnable<LevelGeneratorType> info) {
		if (WorldType.STR_TO_WT_MAP.containsKey(name)) {
			info.setReturnValue(WorldType.STR_TO_WT_MAP.get(name).generatorType);
		}
	}
}