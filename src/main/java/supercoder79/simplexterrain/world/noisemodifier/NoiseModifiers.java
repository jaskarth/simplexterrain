package supercoder79.simplexterrain.world.noisemodifier;

import supercoder79.simplexterrain.api.noise.NoiseModifier;

public enum NoiseModifiers {
	PEAKS(new PeaksNoiseModifier()),
	MESA(new MesaNoiseModifier()),
	SANDBARS(new SandbarNoiseModifier()),
	FJORDS(new FjordNoiseModifier());

	public NoiseModifier noiseModifier;

	NoiseModifiers(NoiseModifier noiseModifier) {
		this.noiseModifier = noiseModifier;
	}
}
