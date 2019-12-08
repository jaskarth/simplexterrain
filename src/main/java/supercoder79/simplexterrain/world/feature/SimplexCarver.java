package supercoder79.simplexterrain.world.feature;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ProbabilityConfig;
import net.minecraft.world.gen.carver.Carver;
import net.minecraft.world.gen.carver.CarverConfig;
import supercoder79.simplexterrain.api.noise.OctaveNoiseSampler;
import supercoder79.simplexterrain.noise.gradient.OpenSimplexNoise;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class SimplexCarver extends Carver<ProbabilityConfig> {
	private OctaveNoiseSampler caveNoise;
	private OctaveNoiseSampler caveHeightNoise;

	List<Long> longs = new ArrayList<>(); //make sure that everything is only carved once

	public SimplexCarver(Function<Dynamic<?>, ? extends ProbabilityConfig> configDeserializer) {
		super(configDeserializer, 256);

	}


//
	@Override
	public boolean carve(Chunk chunk, Function function, Random random, int chunkX, int chunkZ, int mainChunkX, int mainChunkZ, int i, BitSet bitSet, ProbabilityConfig carverConfig) {
		if (longs.contains((((long)mainChunkX) << 32) | (mainChunkZ & 0xffffffffL))) return true;
		if (caveNoise == null) {
			caveNoise = new OctaveNoiseSampler<>(OpenSimplexNoise.class, random, 6, 200, 14, 14);
			caveHeightNoise = new OctaveNoiseSampler<>(OpenSimplexNoise.class, random, 5, Math.pow(2, 6), 10, 10);
		}

		BlockPos.Mutable mutable = new BlockPos.Mutable();
		for (int x = 0; x < 16; x++) {
			mutable.setX(x);
			for (int z = 0; z < 16; z++) {
				mutable.setZ(z);
				int heightOffset = (int)(caveHeightNoise.sample((mainChunkX*16)+x, (mainChunkZ*16)+z))+30;
				int bottom = (int)caveNoise.sample((mainChunkX*16)+x, (mainChunkZ*16)+z) + heightOffset;
				int top = bottom + (int)caveNoise.sample((mainChunkZ*16)+z, (mainChunkX*16)+x)*2;
				double[] vals = new double[]{bottom, top};
				for (int y = 0; y < 256; y++) {
					if (y >= vals[0] && y <= vals[1]) {
						mutable.setY(y);
						if (canCarveBlock(chunk.getBlockState(mutable), chunk.getBlockState(mutable.up())))
							chunk.setBlockState(mutable, Blocks.CAVE_AIR.getDefaultState(), false);
						if (y == vals[1]) break; //we've reached the top, end and continue
					}
				}
			}
		}

		longs.add((((long)mainChunkX) << 32) | (mainChunkZ & 0xffffffffL));

		return true;
	}

	@Override
	public boolean shouldCarve(Random random, int chunkX, int chunkZ, ProbabilityConfig config) {
		return random.nextFloat() <= config.probability;
//		return true;
	}

	@Override
	protected boolean isPositionExcluded(double scaledRelativeX, double scaledRelativeY, double scaledRelativeZ, int y) {
		return scaledRelativeY <= -0.7D || scaledRelativeX * scaledRelativeX + scaledRelativeY * scaledRelativeY + scaledRelativeZ * scaledRelativeZ >= 1.0D;
	}
}
