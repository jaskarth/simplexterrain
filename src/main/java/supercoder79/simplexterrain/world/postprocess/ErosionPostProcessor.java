package supercoder79.simplexterrain.world.postprocess;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkRandom;
import supercoder79.simplexterrain.SimplexTerrain;
import supercoder79.simplexterrain.api.Heightmap;
import supercoder79.simplexterrain.api.noise.OctaveNoiseSampler;
import supercoder79.simplexterrain.api.postprocess.TerrainPostProcessor;
import supercoder79.simplexterrain.noise.gradient.OpenSimplexNoise;

import java.util.Random;

public class ErosionPostProcessor implements TerrainPostProcessor {
	private OctaveNoiseSampler sampler;

	public ErosionPostProcessor(long seed) {
		sampler = new OctaveNoiseSampler<>(OpenSimplexNoise.class, new ChunkRandom(seed), 4, Math.pow(2, 7), 6, 8);
	}

	@Override
	public void postProcess(IWorld world, Random rand, int chunkX, int chunkZ, Heightmap heightmap) {
		int[] heights = heightmap.getHeightInChunk(new ChunkPos(chunkX, chunkZ));
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		for (int x = 0; x < 16; x++) {
			mutable.setX(chunkX*16 + x);
			for (int z = 0; z < 16; z++) {
				mutable.setZ(chunkZ*16 + z);
				double sample = sampler.sample(chunkX*16 + x, chunkZ*16 + z)+0.1;
				if (sample < 0 && heights[x*16 + z] > SimplexTerrain.CONFIG.seaLevel) {
					for (int y = 0; y < Math.abs(sample); y++) {
						mutable.setY(heights[x*16 + z] - y);
						world.setBlockState(mutable, Blocks.AIR.getDefaultState(), 2);
					}
					if (world.getBlockState(mutable.down()) == Blocks.DIRT.getDefaultState()) world.setBlockState(mutable.down(), Blocks.GRASS_BLOCK.getDefaultState(), 2);

				}
			}
		}
	}
}
