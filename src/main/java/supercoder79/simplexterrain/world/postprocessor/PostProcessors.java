package supercoder79.simplexterrain.world.postprocessor;

import supercoder79.simplexterrain.api.postprocess.TerrainPostProcessor;

public enum PostProcessors {
//	RIVERS(new OldRiverPostProcessor()),
	SIMPLEX_CAVES(new SimplexCavesPostProcessor()),
	EROSION(new ErosionPostProcessor()),
	STRATA(new StrataPostProcessor()),
	SOIL(new SoilPostProcessor());

	public TerrainPostProcessor postProcessor;

	PostProcessors(TerrainPostProcessor postProcessor) {
		this.postProcessor = postProcessor;
	}
}
	