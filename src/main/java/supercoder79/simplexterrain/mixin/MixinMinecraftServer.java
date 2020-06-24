package supercoder79.simplexterrain.mixin;

import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.datafixers.DataFixer;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ServerResourceManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListenerFactory;
import net.minecraft.util.UserCache;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryTracker;
import net.minecraft.world.SaveProperties;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.level.storage.LevelStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import supercoder79.simplexterrain.SimplexTerrain;
import supercoder79.simplexterrain.world.gen.SimplexChunkGenerator;

import java.net.Proxy;
import java.util.List;
import java.util.Random;

@Mixin(MinecraftServer.class)
public class MixinMinecraftServer {
    @Inject(method = "<init>", at = @At("RETURN"))
    private void hookConstructor(Thread thread, RegistryTracker.Modifiable modifiable, LevelStorage.Session session, SaveProperties saveProperties, ResourcePackManager<ResourcePackProfile> resourcePackManager, Proxy proxy, DataFixer dataFixer, ServerResourceManager serverResourceManager, MinecraftSessionService minecraftSessionService, GameProfileRepository gameProfileRepository, UserCache userCache, WorldGenerationProgressListenerFactory worldGenerationProgressListenerFactory, CallbackInfo ci) {
        if (saveProperties.getGeneratorOptions().getChunkGenerator() instanceof SimplexChunkGenerator) {
            SimplexTerrain.isSimplexEnabled = true;
        } else {
            SimplexTerrain.isSimplexEnabled = false;
        }
    }

    @Redirect(method = "setupSpawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/source/BiomeSource;locateBiome(IIIILjava/util/List;Ljava/util/Random;)Lnet/minecraft/util/math/BlockPos;"))
    private static BlockPos fixDumbServerCrash(BiomeSource biomeSource, int x, int y, int z, int radius, List<Biome> biomes, Random random) {
        //TODO: unfuck this code
        try {
            return biomeSource.locateBiome(x, y, z, radius, biomes, random);
        } catch (Exception e) {
            System.out.println("[Simplex Terrain] If your server stalls here i'm really sorry but you're gonna have to kill it and restart it.");
            System.out.println("[Simplex Terrain] I have no clue why this happens but it does.");
            System.out.println("[Simplex Terrain] I'll try to get it fixed, but for now this is the workaround - SuperCoder79");
            return new BlockPos(0, 63, 0);
        }
    }
}
