package supercoder79.simplexterrain.mixin.antibad;

//AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAa
//This makes me super fucking triggered

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.CubicSampler;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BackgroundRenderer.class)
@Environment(EnvType.CLIENT)
public class MixinFixStupidCrash {
    private static ClientWorld world;
    private static float value;
    private static Vec3d cache = new Vec3d(0, 0, 0);;

    @Inject(method = "render", at = @At("HEAD"))
    private static void collectParams(Camera camera, float f, ClientWorld clientWorld, int i, float g, CallbackInfo ci) {
        world = clientWorld;
        value = f;
    }

    @Redirect(method = "render",
    at = @At(target = "Lnet/minecraft/util/CubicSampler;method_24895(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/CubicSampler$class_4859;)Lnet/minecraft/util/math/Vec3d;", value = "INVOKE"))
    private static Vec3d irritatered(Vec3d vec3d, CubicSampler.class_4859 arg) {
        Vec3d vec3d3 = cache;
        try {
            vec3d3 = CubicSampler.method_24895(vec3d, (ix, j, kx) ->
                    world.dimension.modifyFogColor(Vec3d.unpackRgb(world.getBiomeAccess().method_24854(ix, j, kx).getFogColor()),
                            MathHelper.clamp(MathHelper.cos(world.getSkyAngle(value) * 6.2831855F) * 2.0F + 0.5F, 0.0F, 1.0F)));
            cache = vec3d3;
        } catch (NullPointerException e) {
            // i literally dont care, please fuck off
        }
        return vec3d3;
    }
}
