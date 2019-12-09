package supercoder79.simplexterrain.mixin.optichunk;

import net.minecraft.block.BlockState;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.PalettedContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import supercoder79.simplexterrain.SimplexTerrain;

@Mixin(PalettedContainer.class)
public class MixinPalettedContainer {
	/**
	 * @reason Do not check the container's lock
	 * @author JellySquid
	 */
	@Inject(method = "lock", at = @At("HEAD"), cancellable = true)
	public void lock(CallbackInfo info) {
		if (SimplexTerrain.CONFIG.optimizeChunkGenerationInvasively) info.cancel();
	}

	/**
	 * @reason Do not check the container's lock
	 * @author JellySquid
	 */
	@Inject(method = "unlock", at = @At("HEAD"), cancellable = true)
	public void unlock(CallbackInfo info) {
		if (SimplexTerrain.CONFIG.optimizeChunkGenerationInvasively) info.cancel();
	}
}