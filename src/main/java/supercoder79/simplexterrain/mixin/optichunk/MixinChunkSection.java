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
	@Shadow @Final private PalettedContainer<BlockState> container;

	@Shadow private short nonEmptyBlockCount;

	@Shadow private short nonEmptyFluidCount;

	@Shadow private short randomTickableBlockCount;

//	@Inject(method = "setBlockState(IIILnet/minecraft/block/BlockState;Z)Lnet/minecraft/block/BlockState;", at = @At("HEAD"), cancellable = true)
//	private void setBlockState(int x, int y, int z, BlockState state, boolean lock, CallbackInfoReturnable<BlockState> info) {
//		if (SimplexTerrain.CONFIG.optimizeChunkGenerationInvasively) {
//			BlockState blockState2 = this.container.set(x, y, z, state);
//
//			if (!blockState2.isAir()) {
//				--this.nonEmptyBlockCount;
//				if (blockState2.hasRandomTicks()) {
//					--this.randomTickableBlockCount;
//				}
//			}
//
//			if (!blockState2.getFluidState().isEmpty()) {
//				--this.nonEmptyFluidCount;
//			}
//
//			if (!state.isAir()) {
//				++this.nonEmptyBlockCount;
//				if (state.hasRandomTicks()) {
//					++this.randomTickableBlockCount;
//				}
//			}
//
//			if (!state.getFluidState().isEmpty()) {
//				++this.nonEmptyFluidCount;
//			}
//
//			info.setReturnValue(blockState2);
//		}
//	}

	@SuppressWarnings("unchecked")
	@Redirect(method = "setBlockState(IIILnet/minecraft/block/BlockState;Z)Lnet/minecraft/block/BlockState;",
			at = @At(target = "Lnet/minecraft/world/chunk/PalettedContainer;setSync(IIILjava/lang/Object;)Ljava/lang/Object;", value = "INVOKE"))
	private Object noLock(PalettedContainer container, int x, int y, int z, Object value) {
		return container.set(x, y, z, value);
	}
}
