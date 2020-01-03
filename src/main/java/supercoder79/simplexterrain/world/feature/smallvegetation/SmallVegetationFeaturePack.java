package supercoder79.simplexterrain.world.feature.smallvegetation;

import net.minecraft.block.Blocks;
import net.minecraft.state.property.Properties;
import net.minecraft.world.biome.Biomes;
import supercoder79.simplexterrain.api.feature.FeaturePack;
import supercoder79.simplexterrain.api.feature.SimplexFeature;

import java.util.Arrays;
import java.util.List;

public class SmallVegetationFeaturePack extends FeaturePack {
	@Override
	public List<SimplexFeature> featuresInPack() {
		return Arrays.asList(
				new ShrubFeature(Blocks.OAK_LOG.getDefaultState(), Blocks.OAK_LEAVES.getDefaultState().with(Properties.DISTANCE_1_7, 1), Biomes.PLAINS, 0, 0.3f),
				new ShrubFeature(Blocks.OAK_LOG.getDefaultState(), Blocks.OAK_LEAVES.getDefaultState().with(Properties.DISTANCE_1_7, 1), Biomes.FOREST, 3, 0.3f),
				new ShrubFeature(Blocks.OAK_LOG.getDefaultState(), Blocks.OAK_LEAVES.getDefaultState().with(Properties.DISTANCE_1_7, 1), Biomes.SWAMP, 2, 0.3f),

				new ShrubFeature(Blocks.BIRCH_LOG.getDefaultState(), Blocks.BIRCH_LEAVES.getDefaultState().with(Properties.DISTANCE_1_7, 1), Biomes.FOREST, 1, 0.3f),
				new ShrubFeature(Blocks.BIRCH_LOG.getDefaultState(), Blocks.BIRCH_LEAVES.getDefaultState().with(Properties.DISTANCE_1_7, 1), Biomes.BIRCH_FOREST, 3, 0.3f),

				new ShrubFeature(Blocks.ACACIA_LOG.getDefaultState(), Blocks.ACACIA_LEAVES.getDefaultState().with(Properties.DISTANCE_1_7, 1), Biomes.SAVANNA, 1, 0.5f),

				new ShrubFeature(Blocks.SPRUCE_LOG.getDefaultState(), Blocks.SPRUCE_LEAVES.getDefaultState().with(Properties.DISTANCE_1_7, 1), Biomes.TAIGA, 0, 0.5f),
				new ShrubFeature(Blocks.SPRUCE_LOG.getDefaultState(), Blocks.SPRUCE_LEAVES.getDefaultState().with(Properties.DISTANCE_1_7, 1), Biomes.MOUNTAINS, 0, 0.5f),
				new ShrubFeature(Blocks.SPRUCE_LOG.getDefaultState(), Blocks.SPRUCE_LEAVES.getDefaultState().with(Properties.DISTANCE_1_7, 1), Biomes.MOUNTAIN_EDGE, 0, 0.5f),
				new ShrubFeature(Blocks.SPRUCE_LOG.getDefaultState(), Blocks.SPRUCE_LEAVES.getDefaultState().with(Properties.DISTANCE_1_7, 1), Biomes.SNOWY_TAIGA, 0, 0.5f),

				new SmallTreeFeature(Blocks.OAK_LOG.getDefaultState(), Blocks.OAK_LEAVES.getDefaultState().with(Properties.DISTANCE_1_7, 1), Biomes.FOREST, 1, 0.3f),
				new SmallTreeFeature(Blocks.OAK_LOG.getDefaultState(), Blocks.OAK_LEAVES.getDefaultState().with(Properties.DISTANCE_1_7, 1), Biomes.SWAMP, 0, 0.1f),
				new SmallTreeFeature(Blocks.SPRUCE_LOG.getDefaultState(), Blocks.SPRUCE_LEAVES.getDefaultState().with(Properties.DISTANCE_1_7, 1), Biomes.TAIGA, 1, 0.3f),
				new SmallTreeFeature(Blocks.SPRUCE_LOG.getDefaultState(), Blocks.SPRUCE_LEAVES.getDefaultState().with(Properties.DISTANCE_1_7, 1), Biomes.MOUNTAIN_EDGE, 1, 0.3f),
				new SmallTreeFeature(Blocks.BIRCH_LOG.getDefaultState(), Blocks.BIRCH_LEAVES.getDefaultState().with(Properties.DISTANCE_1_7, 1), Biomes.BIRCH_FOREST, 1, 0.3f),

				new SpruceTreeFeature(),

				new BoulderFeature(),
				new MangroveTreeFeature(),
				new ToadstoolFeature(Blocks.BROWN_MUSHROOM_BLOCK.getDefaultState()),
				new ToadstoolFeature(Blocks.RED_MUSHROOM_BLOCK.getDefaultState())
		);
	}
}
