package supercoder79.simplexterrain.api.noise;

import supercoder79.simplexterrain.api.noise.Noise;
import supercoder79.simplexterrain.noise.OpenSimplexNoise;
import supercoder79.simplexterrain.noise.PerlinNoise;
import supercoder79.simplexterrain.noise.ValueNoise;
import supercoder79.simplexterrain.noise.WorleyNoise;

public enum NoiseType {
	SIMPLEX(OpenSimplexNoise.class),
	WORLEY(WorleyNoise.class),
	PERLIN(PerlinNoise.class),
	VALUE(ValueNoise.class);

	public final Class<? extends Noise> noiseClass;

	NoiseType(Class<? extends Noise> noiseClass) {
		this.noiseClass = noiseClass;
	}
}
