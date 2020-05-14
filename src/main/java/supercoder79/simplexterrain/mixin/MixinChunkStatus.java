package supercoder79.simplexterrain.mixin;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import supercoder79.simplexterrain.world.gen.SimplexChunkGenerator;

import java.util.List;

@SuppressWarnings("UnresolvedMixinReference") //synthetic lambda method
@Mixin(ChunkStatus.class)
public class MixinChunkStatus {

    @Inject(method = "method_16563", at = @At("HEAD"), remap = false)
    private static void carveSanely(ServerWorld world, ChunkGenerator generator, List<Chunk> list, Chunk chunk, CallbackInfo info) {
        if (generator instanceof SimplexChunkGenerator) {
            ((SimplexChunkGenerator)generator).carvePostProcessors(new ChunkRegion(world, list), chunk);
        }
    }
}
