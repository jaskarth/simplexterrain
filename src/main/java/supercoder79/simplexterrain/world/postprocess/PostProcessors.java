package supercoder79.simplexterrain.world.postprocess;

import supercoder79.simplexterrain.api.postprocess.TerrainPostProcessor;

import java.util.function.Function;
import java.util.function.LongFunction;

public enum PostProcessors {
	RIVERS(new RiverPostProcessor()),
	SIMPLEX_CAVES(new CavePostProcessor()),
	EROSION(new ErosionPostProcessor()),
	STRATA(new StrataPostProcessor()),
	SOIL(new SoilPostProcessor());

	public TerrainPostProcessor postProcessor;

	PostProcessors(TerrainPostProcessor postProcessor) {
		this.postProcessor = postProcessor;
	}
}
