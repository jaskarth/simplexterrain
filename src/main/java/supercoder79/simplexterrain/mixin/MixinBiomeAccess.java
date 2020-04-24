package supercoder79.simplexterrain.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.source.BiomeAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import supercoder79.simplexterrain.SimplexTerrain;
import supercoder79.simplexterrain.world.gen.SimplexBiomeSource;

@Mixin(BiomeAccess.class)
public class MixinBiomeAccess {
    @Shadow @Final private BiomeAccess.Storage storage;

    /**
     * @author
     */
    @Environment(EnvType.CLIENT)
    @Overwrite
    public Biome getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
        // real tired of this BS ngl
        if (this.storage instanceof SimplexBiomeSource) {
            return Biomes.OCEAN;
        }
        return this.storage.getBiomeForNoiseGen(biomeX, biomeY, biomeZ);
    }
}
