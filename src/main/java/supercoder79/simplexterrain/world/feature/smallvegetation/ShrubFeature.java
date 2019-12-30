package supercoder79.simplexterrain.world.feature.smallvegetation;

import net.minecraft.block.BlockState;
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

import java.util.*;

public class ShrubFeature extends SimplexFeature {
	private final BlockState woodState;
	private final BlockState leafState;
	private final Biome biome;
	private final int rarityRegular;
	private final float bonusChance;

	public ShrubFeature(BlockState woodState, BlockState leafState, Biome biome, int rarityRegular, float bonusChance) {
		this.woodState = woodState;
		this.leafState = leafState;
		this.biome = biome;
		this.rarityRegular = rarityRegular;
		this.bonusChance = bonusChance;
	}

	@Override
	public boolean generate(IWorld world, ChunkGenerator<? extends ChunkGeneratorConfig> generator, Random random, BlockPos pos, DefaultFeatureConfig config) {
		if (world.getBlockState(pos.down()) != Blocks.GRASS_BLOCK.getDefaultState()) return true;
//		System.out.println("Generating feature");
		world.setBlockState(pos, woodState, 2);

		setIfAir(world, pos.up(), leafState);

		setIfAir(world, pos.north(), leafState);
		setIfAir(world, pos.south(), leafState);
		setIfAir(world, pos.west(), leafState);
		setIfAir(world, pos.east(), leafState);
		return true;
	}

	@Override
	public Set<Biome> generatesIn() {
		return new HashSet<>(Collections.singletonList(biome));
	}

	@Override
	public ConfiguredDecorator configureDecorator() {
		return Decorator.COUNT_EXTRA_HEIGHTMAP.configure(new CountExtraChanceDecoratorConfig(rarityRegular, bonusChance, 1));
	}

	@Override
	public GenerationStep.Feature generationStep() {
		return GenerationStep.Feature.VEGETAL_DECORATION;
	}
}
