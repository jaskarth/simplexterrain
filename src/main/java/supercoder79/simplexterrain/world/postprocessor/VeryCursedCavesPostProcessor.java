package supercoder79.simplexterrain.world.postprocessor;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkRandom;
import supercoder79.simplexterrain.api.Heightmap;
import supercoder79.simplexterrain.api.cache.AbstractSampler;
import supercoder79.simplexterrain.api.cache.CacheSampler;
import supercoder79.simplexterrain.api.noise.OctaveNoiseSampler;
import supercoder79.simplexterrain.api.postprocess.TerrainPostProcessor;
import supercoder79.simplexterrain.noise.gradient.OpenSimplexNoise;

import java.util.Random;

public class VeryCursedCavesPostProcessor implements TerrainPostProcessor {
    private AbstractSampler sampler;

    @Override
    public void init(long seed) {
        sampler = CacheSampler.makeCacheSampler(new OctaveNoiseSampler<>(OpenSimplexNoise.class, new ChunkRandom(seed), 1, 50, 30, -30));
    }

    @Override
    public void setup() {

    }

    @Override
    public void process(IWorld world, Random rand, int chunkX, int chunkZ, Heightmap heightmap) {
        int[] heights = heightmap.getHeightsInChunk(new ChunkPos(chunkX, chunkZ));

        BlockPos.Mutable pos = new BlockPos.Mutable();
        for (int x = 0; x < 16; x++) {
            pos.setX(chunkX*16 + x);
            for (int z = 0; z < 16; z++) {
                pos.setZ(chunkZ*16 + z);
                for (int y = 0; y < heights[x*16 + z]; y++) {
                    if (sampler.sample(pos.getX(), y, pos.getZ()) > 0) {
                        pos.setY(y);
                        world.setBlockState(pos, Blocks.NETHERRACK.getDefaultState(), 0);
                    }
                }
            }
        }
    }
}
