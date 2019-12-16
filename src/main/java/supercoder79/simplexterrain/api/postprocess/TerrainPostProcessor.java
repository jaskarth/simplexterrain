package supercoder79.simplexterrain.api.postprocess;

import net.minecraft.world.IWorld;
import supercoder79.simplexterrain.api.Heightmap;

import java.util.Random;

public interface TerrainPostProcessor {
	void postProcess(IWorld world, Random rand, int chunkX, int chunkZ, Heightmap heightmap);
}
