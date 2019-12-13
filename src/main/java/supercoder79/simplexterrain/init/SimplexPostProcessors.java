package supercoder79.simplexterrain.init;

import supercoder79.simplexterrain.SimplexTerrain;
import supercoder79.simplexterrain.world.postprocess.ErosionPostProcessor;
import supercoder79.simplexterrain.world.postprocess.PostProcessors;
import supercoder79.simplexterrain.world.gen.SimplexChunkGenerator;

public class SimplexPostProcessors {
	public static void init() {
		for (PostProcessors postProcess : SimplexTerrain.CONFIG.postProcessors) {
			SimplexChunkGenerator.addTerrainPostProcessor(postProcess.factory);
		}
	}
}