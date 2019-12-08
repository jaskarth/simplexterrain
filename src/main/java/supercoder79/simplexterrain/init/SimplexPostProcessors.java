package supercoder79.simplexterrain.init;

import supercoder79.simplexterrain.world.gen.SimplexChunkGenerator;
import supercoder79.simplexterrain.world.postprocess.RiverPostProcessor;

public class SimplexPostProcessors {
	public static void init() {
		SimplexChunkGenerator.addTerrainPostProcessor(RiverPostProcessor::new);
	}
}
