package supercoder79.simplexterrain.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import supercoder79.simplexterrain.client.GoVote;

@Mixin(MinecraftClient.class)
public class MixinMinecraftClient {

    @Inject(method = "openScreen", at = @At("HEAD"), cancellable = true)
    private void handleVoteScreen(Screen screen, CallbackInfo ci) {
        // Handle the go vote screen. Go vote. Please.
        if (GoVote.show((MinecraftClient)(Object)this, screen)) {
            ci.cancel();
        }
    }
}
