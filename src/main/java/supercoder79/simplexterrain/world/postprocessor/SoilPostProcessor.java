package supercoder79.simplexterrain.world.postprocessor;

import java.nio.file.Paths;
import java.util.Random;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkRandom;
import supercoder79.simplexterrain.api.Heightmap;
import supercoder79.simplexterrain.api.noise.OctaveNoiseSampler;
import supercoder79.simplexterrain.api.postprocess.TerrainPostProcessor;
import supercoder79.simplexterrain.configs.ConfigUtil;
import supercoder79.simplexterrain.configs.postprocessors.SoilConfigData;
import supercoder79.simplexterrain.noise.gradient.OpenSimplexNoise;

public class SoilPostProcessor implements TerrainPostProcessor {
	private SoilConfigData config;
	private OctaveNoiseSampler coarseDirtSampler;
	private OctaveNoiseSampler podzolSampler;

	@Override
	public void init(long seed) {
		coarseDirtSampler = new OctaveNoiseSampler<>(OpenSimplexNoise.class, new ChunkRandom(seed + 42), config.coarseDirtOctaves, config.coarseDirtFrequency, config.coarseDirtAmplitudeHigh, config.coarseDirtAmplitudeLow);
		podzolSampler = new OctaveNoiseSampler<>(OpenSimplexNoise.class, new ChunkRandom(seed - 42), config.podzolOctaves, config.podzolFrequency, config.podzolAmplitudeHigh, config.podzolAmplitudeLow);
	}

	@Override
	public void setup() {
		config = ConfigUtil.getFromConfig(SoilConfigData.class, Paths.get("config", "simplexterrain", "postprocessors", "soil.json"));
	}

	@Override
	public void process(IWorld world, Random rand, int chunkX, int chunkZ, Heightmap heightmap) {
		int[] height = heightmap.getHeightsInChunk(new ChunkPos(chunkX, chunkZ));

		BlockPos.Mutable mutable = new BlockPos.Mutable();
		for (int x = 0; x < 16; x++) {
			mutable.setX(chunkX*16 + x);
			for (int z = 0; z < 16; z++) {
				mutable.setZ(chunkZ*16 + z);
				mutable.setY(height[x*16 + z]);

				if (world.getBlockState(mutable) == Blocks.GRASS_BLOCK.getDefaultState()) {
					if (coarseDirtSampler.sample(mutable.getX(), mutable.getZ()) > config.coarseDirtThreshold)
						world.setBlockState(mutable, Blocks.COARSE_DIRT.getDefaultState(), 0);
					if (podzolSampler.sample(mutable.getX(), mutable.getZ()) < config.podzolThreshold)
						world.setBlockState(mutable, Blocks.PODZOL.getDefaultState(), 0);
				}
			}
		}
	}
}
