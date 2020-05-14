package supercoder79.simplexterrain.mixin;

import net.minecraft.block.Blocks;
import net.minecraft.class_5284;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import supercoder79.simplexterrain.noise.gradient.SimplexStyleNoise;
import supercoder79.simplexterrain.world.gen.SimplexNetherGeneration;

import java.util.HashMap;
import java.util.Map;

@Mixin(CavesChunkGenerator.class)
public abstract class MixinCavesChunkGenerator extends SurfaceChunkGenerator {

    @Shadow @Final private double[] noiseFalloff;

    public MixinCavesChunkGenerator(BiomeSource biomeSource, long l, class_5284 arg, int i, int j, int k, boolean bl) {
        super(biomeSource, l, arg, i, j, k, bl);
    }


    @Inject(method = "<init>", at = @At("RETURN"))
    private void constructor(BiomeSource biomeSource, long l, CavesChunkGeneratorConfig cavesChunkGeneratorConfig, CallbackInfo ci) {
        SimplexNetherGeneration.init(l);
    }

    @Override
    public void populateNoise(IWorld world, StructureAccessor structureAccessor, Chunk chunk) {
        SimplexNetherGeneration.generate(world, chunk, this.biomeSource, getSeaLevel());
    }

    @Override
    public double[] computeNoiseRange(int x, int z) {
        return new double[]{0.0D, 0.0D};
    }

    @Override
    public double computeNoiseFalloff(double depth, double scale, int y) {
        return this.noiseFalloff[y];
    }

    @Override
    public void sampleNoiseColumn(double[] buffer, int x, int z) {

    }

    @Override
    public int getSpawnHeight() {
        return 0;
    }

    @Override
    public int getSeaLevel() {
        return 32;
    }
}
