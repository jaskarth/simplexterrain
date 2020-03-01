package supercoder79.simplexterrain.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import supercoder79.simplexterrain.SimplexTerrain;

import net.minecraft.client.gui.screen.world.CreateWorldScreen;

@Mixin(CreateWorldScreen.class)
public class MixinCreateWorldScreen {
	@Shadow
	private int generatorType;

	@Inject(method = "init", at = @At("HEAD"))
	void init(CallbackInfo ci) {
		if (SimplexTerrain.CONFIG.simplexIsDefault) generatorType = SimplexTerrain.SIMPLEX_LEVELGEN.getId();
	}
}
