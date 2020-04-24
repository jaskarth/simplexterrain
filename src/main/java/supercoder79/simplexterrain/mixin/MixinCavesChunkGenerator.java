package supercoder79.simplexterrain.mixin;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.CavesChunkGenerator;
import net.minecraft.world.gen.chunk.CavesChunkGeneratorConfig;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import net.minecraft.world.gen.chunk.SurfaceChunkGenerator;
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
public class MixinCavesChunkGenerator extends SurfaceChunkGenerator {

    @Shadow @Final private double[] noiseFalloff;

    public MixinCavesChunkGenerator(IWorld world, BiomeSource biomeSource, int verticalNoiseResolution, int horizontalNoiseResolution, int worldHeight, ChunkGeneratorConfig config, boolean useSimplexNoise) {
        super(world, biomeSource, verticalNoiseResolution, horizontalNoiseResolution, worldHeight, config, useSimplexNoise);
    }


    @Inject(method = "<init>", at = @At("RETURN"))
    private void constructor(IWorld world, BiomeSource biomeSource, CavesChunkGeneratorConfig config, CallbackInfo ci) {
        SimplexNetherGeneration.init(world.getSeed());
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
