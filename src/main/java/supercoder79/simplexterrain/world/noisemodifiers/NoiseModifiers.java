package supercoder79.simplexterrain.world.noisemodifiers;

import supercoder79.simplexterrain.api.noise.NoiseModifier;

public enum NoiseModifiers {
	PEAKS(new PeaksNoiseModifier()),
	SANDBARS(new SandbarNoiseModifier());

	public NoiseModifier noiseModifier;

	NoiseModifiers(NoiseModifier noiseModifier) {
		this.noiseModifier = noiseModifier;
	}
}
