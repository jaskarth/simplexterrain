package supercoder79.simplexterrain.world.postprocessor;

import supercoder79.simplexterrain.api.postprocess.PostProcessorType;
import supercoder79.simplexterrain.api.postprocess.TerrainPostProcessor;

public enum PostProcessors {
	RIVERS(new RiverPostProcessor(), PostProcessorType.POST),
	SIMPLEX_CAVES(new CavePostProcessor(), PostProcessorType.POST),
	EROSION(new ErosionPostProcessor(), PostProcessorType.POST),
	STRATA(new StrataPostProcessor(), PostProcessorType.POST),
	SOIL(new SoilPostProcessor(), PostProcessorType.POST);

	public TerrainPostProcessor postProcessor;
	public PostProcessorType type;

	PostProcessors(TerrainPostProcessor postProcessor, PostProcessorType type) {
		this.postProcessor = postProcessor;
		this.type = type;
	}
}
	