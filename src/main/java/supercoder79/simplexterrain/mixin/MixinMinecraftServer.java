package supercoder79.simplexterrain.mixin;

import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.datafixers.DataFixer;
import net.minecraft.class_5219;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListenerFactory;
import net.minecraft.server.command.CommandManager;
import net.minecraft.util.UserCache;
import net.minecraft.world.level.storage.LevelStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import supercoder79.simplexterrain.SimplexTerrain;
import supercoder79.simplexterrain.world.gen.SimplexChunkGenerator;

import java.net.Proxy;

@Mixin(MinecraftServer.class)
public class MixinMinecraftServer {
    @Inject(method = "<init>", at = @At("RETURN"))
    private void hookConstructor(LevelStorage.Session session, class_5219 arg, Proxy proxy, DataFixer dataFixer, CommandManager commandManager, MinecraftSessionService minecraftSessionService, GameProfileRepository gameProfileRepository, UserCache userCache, WorldGenerationProgressListenerFactory worldGenerationProgressListenerFactory, CallbackInfo ci) {
        if (arg.method_28057().method_28032() instanceof SimplexChunkGenerator) {
            SimplexTerrain.isSimplexEnabled = true;
        } else {
            SimplexTerrain.isSimplexEnabled = false;
        }
    }
}
