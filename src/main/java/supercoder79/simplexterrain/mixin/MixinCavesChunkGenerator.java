package supercoder79.simplexterrain.mixin;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
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
import supercoder79.simplexterrain.noise.gradient.OpenSimplexNoise;
import supercoder79.simplexterrain.noise.gradient.SimplexStyleNoise;

@Mixin(CavesChunkGenerator.class)
public class MixinCavesChunkGenerator extends SurfaceChunkGenerator {

    @Shadow @Final private double[] noiseFalloff;

    private SimplexStyleNoise noise;

    public MixinCavesChunkGenerator(IWorld world, BiomeSource biomeSource, int verticalNoiseResolution, int horizontalNoiseResolution, int worldHeight, ChunkGeneratorConfig config, boolean useSimplexNoise) {
        super(world, biomeSource, verticalNoiseResolution, horizontalNoiseResolution, worldHeight, config, useSimplexNoise);
    }


    @Inject(method = "<init>", at = @At("RETURN"))
    private void constructor(World world, BiomeSource biomeSource, CavesChunkGeneratorConfig config, CallbackInfo ci) {
        noise = new SimplexStyleNoise(world.getSeed());
    }

    @Override
    public void populateNoise(IWorld world, Chunk chunk) {
        BlockPos.Mutable posMutable = new BlockPos.Mutable();

        for (int x = 0; x < 16; x++) {
            posMutable.setX(x);

            for (int z = 0; z < 16; z++) {
                posMutable.setZ(z);

                for (int y = 0; y < 127; y++) {
                    posMutable.setY(y);
                    if (getThresholdAt((chunk.getPos().x*16) + x, y, (chunk.getPos().z*16) + z) > 0.25) {
                        chunk.setBlockState(posMutable, Blocks.NETHERRACK.getDefaultState(), false);
                    } else if (y < this.getSeaLevel()) {
                        chunk.setBlockState(posMutable, Blocks.LAVA.getDefaultState(), false);
                    }
                }
            }
        }
    }

    private double getThresholdAt(int x, int y, int z) {
        double baseline = noise.sample(x / 70f, y / 35f, z / 70f);
        baseline += (12 / (float)y); //lower bound
        baseline += (-12 / ((float)(y - 130))); //upper bound
        return baseline;
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
