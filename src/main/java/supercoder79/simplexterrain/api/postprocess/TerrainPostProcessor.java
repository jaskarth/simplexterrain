package supercoder79.simplexterrain.api.postprocess;

import java.util.Random;

import net.minecraft.world.IWorld;

public interface TerrainPostProcessor {
	void postProcess(IWorld world, Random rand, int chunkX, int chunkZ);
}
