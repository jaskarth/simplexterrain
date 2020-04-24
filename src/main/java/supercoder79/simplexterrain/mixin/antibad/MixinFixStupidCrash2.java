package supercoder79.simplexterrain.mixin.antibad;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.sound.BiomeEffectSoundPlayer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.source.BiomeAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Environment(EnvType.CLIENT)
@Mixin(BiomeEffectSoundPlayer.class)
public class MixinFixStupidCrash2 {

    @Shadow @Final private BiomeAccess biomeAccess;

    @Shadow @Final private ClientPlayerEntity player;

    @Redirect(method = "tick",
            at = @At(target = "Lnet/minecraft/world/biome/source/BiomeAccess;getBiome(DDD)Lnet/minecraft/world/biome/Biome;", value = "INVOKE"))
    private Biome pleaseNoNull(BiomeAccess biomeAccess, double d, double e, double f) {
        Biome biome = this.biomeAccess.getBiome(this.player.getX(), this.player.getY(), this.player.getZ());
        if (biome == null) return Biomes.PLAINS;
        return biome;
    }
}
