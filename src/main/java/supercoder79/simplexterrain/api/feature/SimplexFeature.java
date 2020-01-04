package supercoder79.simplexterrain.api.feature;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import net.minecraft.world.gen.decorator.ConfiguredDecorator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;

import java.util.Random;
import java.util.Set;

/**
 * A slightly abstracted Feature that takes care of decoration and configuring.
 * It's limited to DefaultFeatureConfig for simplicity reasons.
 *
 * @author SuperCoder79
 */
public abstract class SimplexFeature extends Feature<DefaultFeatureConfig> {
	public SimplexFeature() {
		super(DefaultFeatureConfig::deserialize);
	}

	/**
	 * The main generation method.
	 * Note: The IWorld given here is a ChunkRegion.
	 */
	@Override
	public abstract boolean generate(IWorld world, ChunkGenerator<? extends ChunkGeneratorConfig> generator, Random random, BlockPos pos, DefaultFeatureConfig config);

	/**
	 * What biomes this feature generates in.
	 */
	public abstract Set<Biome> generatesIn();

	/**
	 * The decorator that dictates the spawning of this feature.
	 */
	public abstract ConfiguredDecorator configureDecorator();

	/**
	 * What feature step this feature generates in.
	 */
	public abstract GenerationStep.Feature generationStep();

	/**
	 * A utility function to configure this feature.
	 */
	public ConfiguredFeature<?, ?> configuredFeature() {
		return this.configure(FeatureConfig.DEFAULT).createDecoratedFeature(this.configureDecorator());
	}

	// ============= Utilities

	protected void setIfAir(IWorld world, BlockPos pos, BlockState state) {
		if (world.getBlockState(pos).isAir()) world.setBlockState(pos, state, 2);
	}
}
