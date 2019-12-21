package supercoder79.simplexterrain.world.postprocessor;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkRandom;
import supercoder79.simplexterrain.api.Heightmap;
import supercoder79.simplexterrain.api.noise.OctaveNoiseSampler;
import supercoder79.simplexterrain.api.postprocess.TerrainPostProcessor;
import supercoder79.simplexterrain.noise.gradient.OpenSimplexNoise;

import java.util.Random;

public class CavePostProcessor implements TerrainPostProcessor {
	private OctaveNoiseSampler caveNoise;
	private OctaveNoiseSampler caveEnabledNoise;
	private OctaveNoiseSampler caveHeightNoise;

	@Override
	public void init(long seed) {
		caveNoise = new OctaveNoiseSampler<>(OpenSimplexNoise.class, new ChunkRandom(seed), 6, 2048, 14, 14);
		caveHeightNoise = new OctaveNoiseSampler<>(OpenSimplexNoise.class, new ChunkRandom(seed), 5, Math.pow(2, 5), 10, 10);
		caveEnabledNoise = new OctaveNoiseSampler<>(OpenSimplexNoise.class, new ChunkRandom(seed + 20), 6, 1024, 1, 1);
	}

	@Override
	public void setup() {

	}

	@Override
	public void process(IWorld world, Random rand, int chunkX, int chunkZ, Heightmap heightmap) {
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		for (int x = 0; x < 16; x++) {
			mutable.setX((chunkX*16)+x);
			for (int z = 0; z < 16; z++) {
				mutable.setZ((chunkZ * 16) + z);

				if (caveEnabledNoise.sample((chunkX * 16) + x, (chunkZ * 16) + z) > 0) {
					int heightOffset = (int) (caveHeightNoise.sample((chunkX * 16) + x, (chunkZ * 16) + z)) + 30;
					int bottom = (int) caveNoise.sample((chunkX * 16) + x, (chunkZ * 16) + z) + heightOffset;
					int top = bottom + (int) caveNoise.sample((chunkZ * 16) + z, (chunkX * 16) + x) * 2;

					if (Math.abs(top - bottom) <= 3) continue;
					double[] vals = new double[]{bottom, top};

					for (int y = 0; y < 256; y++) {
						if (y >= vals[0] && y <= vals[1]) {
							mutable.setY(y);
							if (world.getBlockState(mutable) != Blocks.WATER.getDefaultState())
								world.setBlockState(mutable, Blocks.CAVE_AIR.getDefaultState(), 2);
							if (y == vals[1]) break; //we've reached the top, end and continue
						}
					}
				}
			}
		}
	}
}
