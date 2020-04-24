package supercoder79.simplexterrain.mixin.antibad;

import net.minecraft.class_5195;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.CreditsScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.sound.MusicTracker;
import net.minecraft.client.sound.MusicType;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.TheEndDimension;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MinecraftClient.class)
public class MixinFixStupidCrash3 {
    @Shadow public Screen currentScreen;

    @Shadow public ClientPlayerEntity player;

    @Shadow @Final public InGameHud inGameHud;

    @Shadow @Final private MusicTracker musicTracker;

    @Shadow public ClientWorld world;

    /**
     * @author
     */
    @Overwrite
    public class_5195 getMusicType() {
        try {
            if (this.currentScreen instanceof CreditsScreen) {
                return MusicType.field_5578;
            } else if (this.player != null) {
                if (this.player.world.dimension instanceof TheEndDimension) {
                    return this.inGameHud.getBossBarHud().shouldPlayDragonMusic() ? MusicType.field_5580 : MusicType.field_5583;
                } else {
                    Biome.Category category = this.player.world.getBiome(this.player.getBlockPos()).getCategory();
                    if (this.musicTracker.isPlayingType(MusicType.field_5576) || this.player.isSubmergedInWater() && (category == Biome.Category.OCEAN || category == Biome.Category.RIVER)) {
                        return MusicType.field_5576;
                    } else {
                        return this.player.abilities.creativeMode && this.player.abilities.allowFlying ? MusicType.field_5581 : (class_5195) this.world.getBiomeAccess().method_27344(this.player.getBlockPos()).method_27343().orElse(MusicType.field_5586);
                    }
                }
            } else {
                return MusicType.field_5585;
            }
        } catch (Exception e) {

        }
        return MusicType.field_5586;
    }
}
