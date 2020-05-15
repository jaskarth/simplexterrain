package supercoder79.simplexterrain.world.postprocessor;

import java.nio.file.Paths;
import java.util.Random;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.ChunkRandom;
import supercoder79.simplexterrain.SimplexTerrain;
import supercoder79.simplexterrain.api.Heightmap;
import supercoder79.simplexterrain.api.cache.AbstractSampler;
import supercoder79.simplexterrain.api.cache.CacheSampler;
import supercoder79.simplexterrain.api.noise.OctaveNoiseSampler;
import supercoder79.simplexterrain.api.postprocess.TerrainPostProcessor;
import supercoder79.simplexterrain.configs.ConfigUtil;
import supercoder79.simplexterrain.configs.postprocessors.ErosionConfigData;
import supercoder79.simplexterrain.noise.gradient.OpenSimplexNoise;

public class ErosionPostProcessor implements TerrainPostProcessor {
	private ErosionConfigData config;
	private OctaveNoiseSampler sampler;

	@Override
	public void init(long seed) {
		sampler = new OctaveNoiseSampler<>(OpenSimplexNoise.class, new ChunkRandom(seed), config.octaves, config.frequency, config.amplitudeHigh, config.amplitudeLow);
	}

	@Override
	public void setup() {
		config = ConfigUtil.getFromConfig(ErosionConfigData.class, Paths.get("config", "simplexterrain", "postprocessors", "erosion.json"));
	}

	@Override
	public void process(WorldAccess world, Random rand, int chunkX, int chunkZ, Heightmap heightmap) {
		int[] heights = heightmap.getHeightsInChunk(new ChunkPos(chunkX, chunkZ));
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		for (int x = 0; x < 16; x++) {
			mutable.setX(chunkX*16 + x);
			for (int z = 0; z < 16; z++) {
				mutable.setZ(chunkZ*16 + z);
				double sample = sampler.sample(chunkX*16 + x, chunkZ*16 + z) + config.baseNoise;
				if (sample < config.threshold && heights[x*16 + z] > SimplexTerrain.CONFIG.seaLevel) {
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
