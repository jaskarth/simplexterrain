package supercoder79.simplexterrain.world.postprocess;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import supercoder79.simplexterrain.api.Coordinate2Function;
import supercoder79.simplexterrain.api.Heightmap;
import supercoder79.simplexterrain.api.postprocess.TerrainPostProcessor;
import supercoder79.simplexterrain.noise.gradient.OpenSimplexNoise;

public final class RiverPostProcessor implements TerrainPostProcessor {
	private final OpenSimplexNoise noiseSampler;

	public RiverPostProcessor(long seed) {
		noiseSampler = new OpenSimplexNoise(seed - 8);
	}

	@Override
	public void postProcess(IWorld world, Random rand, int chunkX, int chunkZ, Heightmap heightmap) {
		BlockPos.Mutable pos = new BlockPos.Mutable();
		int startX = (chunkX << 4);
		int startZ = (chunkZ << 4);

		for (int localX = 0; localX < 16; ++localX) {
			int x = localX + startX;
			pos.setX(x);
			for (int localZ = 0; localZ < 16; ++localZ) {
				int z = localZ + startZ;
				pos.setZ(z);
				double noise = noiseSampler.sample((double) x / 310.0, (double) z / 310.0);
				if (noise > 0.12 && noise < 0.15) {
					addRiverInChunk(world, heightmap::getHeight, x, z, pos, 0.13 - noise);
				}
			}
		}
	}

	private void addRiverInChunk(IWorld world, Coordinate2Function<Integer> heightFunction, final int x, final int z, BlockPos.Mutable pos, double depthNoise) {
		int y = heightFunction.apply(x, z);

		if (y < world.getSeaLevel()) {
			return;
		}
		++y;

		if (depthNoise < 0) {
			depthNoise *= -1;
		}
		depthNoise = 0.1 - depthNoise;
		depthNoise *= 5 * 10;
		int depth = (int) depthNoise;

		pos.setY(y);
		BlockState bottomBlock = world.getBlockState(pos);

		int waterLevel = calculateWaterLevel(x, z, heightFunction);

		for (int yOffset = 0; yOffset <= depth; ++yOffset) {
			--y;
			pos.setY(y);

			if (yOffset == 1) {
				bottomBlock = world.getBlockState(pos); 
			}

			world.setBlockState(pos, yOffset > waterLevel ? AIR : WATER, 2);
		}

		pos.setY(y - 1);
		world.setBlockState(pos, bottomBlock, 2);
	}

	private int calculateWaterLevel(int x, int z, Coordinate2Function<Integer> heightFunction) {
		int subX = ((x >> 2) << 2);
		int subZ = ((z >> 2) << 2);
		int subXUpper = subX + 4;
		int subZUpper = subZ + 4;

		double xProgress = (double) (x - subX) / 4.0;
		double zProgress = (double) (z - subZ) / 4.0;

		double sampleNW = heightFunction.apply(subX, subZ);
		double sampleNE = heightFunction.apply(subXUpper, subZ);
		double sampleSW = heightFunction.apply(subX, subZUpper);
		double sampleSE = heightFunction.apply(subXUpper, subZUpper);

		return (int) MathHelper.lerp(zProgress,
				MathHelper.lerp(xProgress, sampleNW, sampleNE),
				MathHelper.lerp(xProgress, sampleSW, sampleSE));
	}

	public static final BlockState AIR = Blocks.AIR.getDefaultState();
	public static final BlockState WATER = Blocks.WATER.getDefaultState();
}
