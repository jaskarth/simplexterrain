package supercoder79.simplexterrain.world.feature.smallvegetation;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import net.minecraft.world.gen.decorator.ConfiguredDecorator;
import net.minecraft.world.gen.decorator.CountExtraChanceDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import supercoder79.simplexterrain.api.feature.SimplexFeature;

import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class BushTreeFeature extends SimplexFeature {
    @Override
    public boolean generate(IWorld world, ChunkGenerator<? extends ChunkGeneratorConfig> generator, Random random, BlockPos pos, DefaultFeatureConfig config) {
        if (world.getBlockState(pos.down()) != Blocks.GRASS_BLOCK.getDefaultState()) return true;

        int height = random.nextInt(3) + 4;

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                world.setBlockState(pos.add(i, height+1, j), Blocks.OAK_LEAVES.getDefaultState(), 2);
            }
        }

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                world.setBlockState(pos.add(i, height, j), Blocks.OAK_LEAVES.getDefaultState(), 2);
            }
        }

        for (int i = 0; i < height; i++) {
            world.setBlockState(pos.add(0, height, 0), Blocks.OAK_LOG.getDefaultState(), 2);
        }

        return true;
    }

    @Override
    public Set<Biome> generatesIn() {
        return new HashSet<>(Collections.singletonList(Biomes.PLAINS));
    }

    @Override
    public ConfiguredDecorator configureDecorator() {
        return Decorator.COUNT_EXTRA_HEIGHTMAP.configure(new CountExtraChanceDecoratorConfig(1, 0.1f, 1));
    }

    @Override
    public GenerationStep.Feature generationStep() {
        return GenerationStep.Feature.VEGETAL_DECORATION;
    }
}
