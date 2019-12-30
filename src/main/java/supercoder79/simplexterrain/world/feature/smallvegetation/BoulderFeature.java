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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class BoulderFeature extends SimplexFeature {
	@Override
	public boolean generate(IWorld world, ChunkGenerator<? extends ChunkGeneratorConfig> generator, Random random, BlockPos pos, DefaultFeatureConfig config) {
		if (world.getBlockState(pos.down()) != Blocks.GRASS_BLOCK.getDefaultState()) return true;
		world.setBlockState(pos, Blocks.STONE.getDefaultState(), 2);
		world.setBlockState(pos.up(), Blocks.STONE.getDefaultState(), 2);
		world.setBlockState(pos.down(), Blocks.STONE.getDefaultState(), 2);
		world.setBlockState(pos.north(), Blocks.STONE.getDefaultState(), 2);
		world.setBlockState(pos.south(), Blocks.STONE.getDefaultState(), 2);
		world.setBlockState(pos.west(), Blocks.STONE.getDefaultState(), 2);
		world.setBlockState(pos.east(), Blocks.STONE.getDefaultState(), 2);
		return true;
	}

	@Override
	public Set<Biome> generatesIn() {
		return new HashSet<>(Arrays.asList(Biomes.FOREST, Biomes.PLAINS));
	}

	@Override
	public ConfiguredDecorator configureDecorator() {
		return Decorator.COUNT_EXTRA_HEIGHTMAP.configure(new CountExtraChanceDecoratorConfig(0, 0.03f, 1));
	}

	@Override
	public GenerationStep.Feature generationStep() {
		return GenerationStep.Feature.VEGETAL_DECORATION;
	}
}
