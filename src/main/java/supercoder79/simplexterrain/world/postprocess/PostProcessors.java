package supercoder79.simplexterrain.world.postprocess;

import supercoder79.simplexterrain.api.postprocess.TerrainPostProcessor;
import supercoder79.simplexterrain.world.postprocess.CavePostProcessor;
import supercoder79.simplexterrain.world.postprocess.RiverPostProcessor;

import java.util.function.LongFunction;

public enum PostProcessors {
	RIVERS(RiverPostProcessor::new),
	SIMPLEX_CAVES(CavePostProcessor::new),
	EROSION(ErosionPostProcessor::new);

	public LongFunction<TerrainPostProcessor> factory;

	PostProcessors(LongFunction<TerrainPostProcessor> factory) {

		this.factory = factory;
	}
}
