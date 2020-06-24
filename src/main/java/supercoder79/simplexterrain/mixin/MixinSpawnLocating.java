package supercoder79.simplexterrain.mixin;

import net.minecraft.server.network.SpawnLocating;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SpawnLocating.class)
public abstract class MixinSpawnLocating {

    @Shadow
    protected static BlockPos findOverworldSpawn(ServerWorld world, int x, int z, boolean validSpawnNeeded) {
        return null;
    }

    @Redirect(method = "findServerSpawnPoint", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/SpawnLocating;findOverworldSpawn(Lnet/minecraft/server/world/ServerWorld;IIZ)Lnet/minecraft/util/math/BlockPos;"))
    private static BlockPos getPos(ServerWorld world, int x, int z, boolean validSpawnNeeded) {
        try {
            return findOverworldSpawn(world, x, z, validSpawnNeeded);
        } catch (Exception e) {
            return new BlockPos(x + 8, world.getSeaLevel(), z + 8);
        }
    }
}
