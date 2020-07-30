package supercoder79.simplexterrain.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.SkyProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import supercoder79.simplexterrain.SimplexTerrain;

@Environment(EnvType.CLIENT)
@Mixin(SkyProperties.class)
public class MixinSkyProperties {
	@Inject(method = "getCloudsHeight", at = @At("HEAD"), cancellable = true)
	private void getCloudsHeight(CallbackInfoReturnable<Float> info) {

		// I don't feel like making simplex terrain send over multiplayer data, so.... for now this is hardcoded to singleplayer
		if (SimplexTerrain.isSimplexEnabled && MinecraftClient.getInstance().isInSingleplayer()) {
			info.setReturnValue((float) SimplexTerrain.CONFIG.cloudHeight);
		}
	}
}