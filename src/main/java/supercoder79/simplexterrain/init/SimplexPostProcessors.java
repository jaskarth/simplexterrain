package supercoder79.simplexterrain.init;

import supercoder79.simplexterrain.SimplexTerrain;
import supercoder79.simplexterrain.world.gen.SimplexChunkGenerator;
import supercoder79.simplexterrain.world.postprocess.PostProcessors;

public class SimplexPostProcessors {
	public static void init() {
		for (PostProcessors postProcess : SimplexTerrain.CONFIG.postProcessors) {
			SimplexChunkGenerator.addTerrainPostProcessor(postProcess.factory);
		}
	}
}