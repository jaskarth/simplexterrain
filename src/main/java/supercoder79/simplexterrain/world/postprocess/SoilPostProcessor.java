package supercoder79.simplexterrain.world.postprocess;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkRandom;
import supercoder79.simplexterrain.api.Heightmap;
import supercoder79.simplexterrain.api.noise.OctaveNoiseSampler;
import supercoder79.simplexterrain.api.postprocess.TerrainPostProcessor;
import supercoder79.simplexterrain.noise.gradient.OpenSimplexNoise;

import java.util.Random;

public class SoilPostProcessor implements TerrainPostProcessor {
	private OctaveNoiseSampler coarseDirtSampler;
	private OctaveNoiseSampler podzolSampler;

	public SoilPostProcessor(long seed) {
		coarseDirtSampler = new OctaveNoiseSampler<>(OpenSimplexNoise.class, new ChunkRandom(seed + 42), 4, Math.pow(2, 8), 6, 8);
		podzolSampler = new OctaveNoiseSampler<>(OpenSimplexNoise.class, new ChunkRandom(seed - 42), 4, Math.pow(2, 8), 6, 8);
	}

	@Override
	public void postProcess(IWorld world, Random rand, int chunkX, int chunkZ, Heightmap heightmap) {
		int[] height = heightmap.getHeightInChunk(new ChunkPos(chunkX, chunkZ));

		BlockPos.Mutable mutable = new BlockPos.Mutable();
		for (int x = 0; x < 16; x++) {
			mutable.setX(chunkX*16 + x);
			for (int z = 0; z < 16; z++) {
				mutable.setZ(chunkZ*16 + z);
				mutable.setY(height[x*16 + z]);

				if (world.getBlockState(mutable) == Blocks.GRASS_BLOCK.getDefaultState()) {
					if (coarseDirtSampler.sample(mutable.getX(), mutable.getZ()) > 0.65)
						world.setBlockState(mutable, Blocks.COARSE_DIRT.getDefaultState(), 0);
					if (podzolSampler.sample(mutable.getX(), mutable.getZ()) < -0.65)
						world.setBlockState(mutable, Blocks.PODZOL.getDefaultState(), 0);
				}
			}
		}
	}
}
