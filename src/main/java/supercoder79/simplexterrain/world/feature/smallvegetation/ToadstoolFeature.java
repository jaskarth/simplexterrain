package supercoder79.simplexterrain.world.feature.smallvegetation;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.MushroomBlock;
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

public class ToadstoolFeature extends SimplexFeature {
	private final BlockState mushroomState;

	public ToadstoolFeature(BlockState mushroomState) {
		this.mushroomState = mushroomState;
	}

	@Override
	public boolean generate(IWorld world, ChunkGenerator<? extends ChunkGeneratorConfig> generator, Random random, BlockPos pos, DefaultFeatureConfig config) {
		if (world.getBlockState(pos.down()) != Blocks.GRASS_BLOCK.getDefaultState()) return true;

		//TODO: set sides for mushroomState
		world.setBlockState(pos, Blocks.MUSHROOM_STEM.getDefaultState().with(MushroomBlock.UP, false).with(MushroomBlock.DOWN, false), 2);
		world.setBlockState(pos.up(), Blocks.MUSHROOM_STEM.getDefaultState().with(MushroomBlock.UP, false).with(MushroomBlock.DOWN, false), 2);
		for (int x = -1; x < 2; x++) {
			for (int z = -1; z < 2; z++) {
				world.setBlockState(pos.add(x, 2, z), mushroomState, 0);
			}
		}

		return true;
	}

	@Override
	public Set<Biome> generatesIn() {
		return new HashSet<>(Collections.singletonList(Biomes.SWAMP));
	}

	@Override
	public ConfiguredDecorator configureDecorator() {
		return Decorator.COUNT_EXTRA_HEIGHTMAP.configure(new CountExtraChanceDecoratorConfig(0, 0.1f, 1));
	}

	@Override
	public GenerationStep.Feature generationStep() {
		return GenerationStep.Feature.VEGETAL_DECORATION;
	}
}
