package supercoder79.simplexterrain.world.postprocessor;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import supercoder79.simplexterrain.api.Heightmap;
import supercoder79.simplexterrain.api.postprocess.TerrainPostProcessor;

import java.util.Random;

public class StrataPostProcessor implements TerrainPostProcessor {
	private static final BlockState[] states = new BlockState[32];

	@Override
	public void init(long seed) {
		for (int i = 0; i < 4; i++) {
			states[i*8] = Blocks.STONE.getDefaultState();
			states[i*8 + 1] = Blocks.ANDESITE.getDefaultState();
			states[i*8 + 2] = Blocks.GRANITE.getDefaultState();
			states[i*8 + 3] = Blocks.GRANITE.getDefaultState();
			states[i*8 + 4] = Blocks.STONE.getDefaultState();
			states[i*8 + 5] = Blocks.DIORITE.getDefaultState();
			states[i*8 + 6] = Blocks.ANDESITE.getDefaultState();
			states[i*8 + 7] = Blocks.STONE.getDefaultState();

		}
	}

	@Override
	public void setup() {

	}

	@Override
	public void process(IWorld world, Random rand, int chunkX, int chunkZ, Heightmap heightmap) {
		int[] heights = heightmap.getHeightInChunk(new ChunkPos(chunkX, chunkZ));
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		for (int x = 0; x < 16; x++) {
			mutable.setX(chunkX*16 + x);
			for (int z = 0; z < 16; z++) {
				mutable.setZ(chunkZ*16 + z);
				for (int y = heights[x* 16 + z]; y > 0; y--) {
					mutable.setY(y);
					if (world.getBlockState(mutable) == Blocks.STONE.getDefaultState()) world.setBlockState(mutable, states[y/8], 2);
				}
			}
		}
	}
}
