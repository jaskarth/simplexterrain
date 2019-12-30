package supercoder79.simplexterrain.world.feature.smallvegetation;

import net.minecraft.block.Blocks;
import net.minecraft.state.property.Properties;
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

public class MangroveTreeFeature extends SimplexFeature {
	@Override
	public boolean generate(IWorld world, ChunkGenerator<? extends ChunkGeneratorConfig> generator, Random random, BlockPos pos, DefaultFeatureConfig config) {
		if (world.getBlockState(pos.down()) != Blocks.GRASS_BLOCK.getDefaultState()) return true;

		int rootHeight = random.nextInt(3) + 1;

		//roots
		for (int i = 0; i < rootHeight; i++) {
			world.setBlockState(pos.add(-1, i, 0), Blocks.OAK_LOG.getDefaultState(), 0);
			world.setBlockState(pos.add(1, i, 0), Blocks.OAK_LOG.getDefaultState(), 0);
			world.setBlockState(pos.add(0, i, -1), Blocks.OAK_LOG.getDefaultState(), 0);
			world.setBlockState(pos.add(0, i, 1), Blocks.OAK_LOG.getDefaultState(), 0);
		}

		int trunkHeight = random.nextInt(4) + 3 + rootHeight;

		//trunk
		for (int i = 0; i < trunkHeight; i++) {
			world.setBlockState(pos.add(0, i, 0), Blocks.OAK_LOG.getDefaultState(), 0);
		}

		for (int x = -1; x < 2; x++) {
			for (int z = -1; z < 2; z++) {
				if (x != 0 && z != 0) //skip the trunk
				world.setBlockState(pos.add(x, trunkHeight - 1, z), Blocks.OAK_LEAVES.getDefaultState().with(Properties.DISTANCE_1_7, 1), 0);
			}
		}

		for (int x = -2; x < 3; x++) {
			for (int z = -2; z < 3; z++) {
				if (!(Math.abs(x) == 2 && Math.abs(z) == 2))
					world.setBlockState(pos.add(x, trunkHeight, z), Blocks.OAK_LEAVES.getDefaultState().with(Properties.DISTANCE_1_7, 1), 0);
			}
		}

		for (int x = -1; x < 2; x++) {
			for (int z = -1; z < 2; z++) {
				if (!(Math.abs(x) == 1 && Math.abs(z) == 1))
					world.setBlockState(pos.add(x, 1 + trunkHeight, z), Blocks.OAK_LEAVES.getDefaultState().with(Properties.DISTANCE_1_7, 1), 0);
			}
		}

		return true;
	}

	@Override
	public Set<Biome> generatesIn() {
		return new HashSet<>(Arrays.asList(Biomes.SWAMP));
	}

	@Override
	public ConfiguredDecorator configureDecorator() {
		return Decorator.COUNT_EXTRA_HEIGHTMAP.configure(new CountExtraChanceDecoratorConfig(0, 0.25f, 1));
	}

	@Override
	public GenerationStep.Feature generationStep() {
		return GenerationStep.Feature.VEGETAL_DECORATION;
	}
}
