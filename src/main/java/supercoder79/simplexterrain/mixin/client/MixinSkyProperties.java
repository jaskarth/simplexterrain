package supercoder79.simplexterrain.mixin.client;

import net.minecraft.client.render.SkyProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import supercoder79.simplexterrain.SimplexTerrain;

@Mixin(SkyProperties.class)
public class MixinSkyProperties {
	@Inject(method = "getCloudsHeight", at = @At("HEAD"), cancellable = true)
	private void getCloudsHeight(CallbackInfoReturnable<Float> info) {
		if (SimplexTerrain.isSimplexEnabled) {
			info.setReturnValue((float) SimplexTerrain.CONFIG.cloudHeight);
		}
	}
}