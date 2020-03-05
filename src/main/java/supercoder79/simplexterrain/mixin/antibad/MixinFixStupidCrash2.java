package supercoder79.simplexterrain.mixin.antibad;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_4897;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.source.BiomeAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Environment(EnvType.CLIENT)
@Mixin(class_4897.class)
public class MixinFixStupidCrash2 {

    @Shadow @Final private ClientPlayerEntity field_22796;

    @Shadow @Final private BiomeAccess field_22798;

    @Redirect(method = "tick",
            at = @At(target = "Lnet/minecraft/world/biome/source/BiomeAccess;method_24938(DDD)Lnet/minecraft/world/biome/Biome;", value = "INVOKE"))
    private Biome pleaseNoNull(BiomeAccess biomeAccess, double d, double e, double f) {
        Biome biome = this.field_22798.method_24938(this.field_22796.getX(), this.field_22796.getY(), this.field_22796.getZ());
        if (biome == null) return Biomes.PLAINS;
        return biome;
    }
}
