package supercoder79.simplexterrain.api;

import net.minecraft.util.math.ChunkPos;
import supercoder79.simplexterrain.world.gen.SimplexChunkGenerator;

/**
 * Any object that's able to retrieve a height from a heightmap.
 *
 * @author Valoeghese and SuperCoder79
 */
public interface Heightmap {
    /**
     * Returns a height value for a x/z coordinate.
     *
     * When iterating over a chunk to get all of the height values, it is almost always preferable to use
     * getHeightInChunk() instead.
     *
     * @param x x value (block position)
     * @param z z value (block position)
     * @return The height value of the heightmap at this x/z coordinate.
     */
    int getHeight(int x, int z);

    /**
     * Returns all of the heights for ever block in this chunk.
     *
     * The default implementation is good enough but ideally it would be threaded and use a cache to reduce
     * access times. {@link SimplexChunkGenerator}
     *
     * @param pos the position of the requested chunk
     * @return an int array of the height values for this chunk.
     */
    default int[] getHeightsInChunk(ChunkPos pos) {
        int[] heights = new int[256];
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                heights[x*16 + z] = getHeight((pos.x * 16) + x, (pos.z * 16) + z);
            }
        }
        return heights;
    }

    /**
     * A heightmap that always returns 0. Useful for a default initialization.
     */
    Heightmap NONE = (x, z) -> 0;
}
