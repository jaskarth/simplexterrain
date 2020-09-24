package supercoder79.simplexterrain.mixin.optichunk;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BlockState;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.PalettedContainer;
import supercoder79.simplexterrain.SimplexTerrain;

@Mixin(ChunkSection.class)
public class MixinChunkSection {

	@SuppressWarnings("unchecked")
	@Redirect(method = "setBlockState(IIILnet/minecraft/block/BlockState;Z)Lnet/minecraft/block/BlockState;",
			at = @At(target = "Lnet/minecraft/world/chunk/PalettedContainer;setSync(IIILjava/lang/Object;)Ljava/lang/Object;", value = "INVOKE"))
	private Object noLock(PalettedContainer container, int x, int y, int z, Object value) {
		return container.set(x, y, z, value);
	}
}
