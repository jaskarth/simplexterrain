package supercoder79.simplexterrain.api.noise;

import supercoder79.simplexterrain.noise.gradient.CubicNoise;
import supercoder79.simplexterrain.noise.gradient.OpenSimplexNoise;
import supercoder79.simplexterrain.noise.gradient.PerlinNoise;
import supercoder79.simplexterrain.noise.gradient.GaborNoise;
import supercoder79.simplexterrain.noise.value.ValueNoise;
import supercoder79.simplexterrain.noise.voronoi.VoronoiNoise;
import supercoder79.simplexterrain.noise.worley.WorleyNoise;

public enum NoiseType {
	SIMPLEX(OpenSimplexNoise.class),
	WORLEY(WorleyNoise.class),
	PERLIN(PerlinNoise.class),
	VALUE(ValueNoise.class),
	CUBIC(CubicNoise.class),
	GABOR(GaborNoise.class),
	VORONOI(VoronoiNoise.class);

	public final Class<? extends Noise> noiseClass;

	NoiseType(Class<? extends Noise> noiseClass) {
		this.noiseClass = noiseClass;
	}
}
