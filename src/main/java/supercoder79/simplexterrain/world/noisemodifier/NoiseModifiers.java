package supercoder79.simplexterrain.world.noisemodifier;

import supercoder79.simplexterrain.api.noisemodifier.NoiseModifier;

public enum NoiseModifiers {
	PEAKS(new PeaksNoiseModifier()),
	MESA(new MesaNoiseModifier()),
	DOMES(new DomeNoiseModifier()),
	SANDBARS(new SandbarNoiseModifier()),
	FJORDS(new FjordNoiseModifier()),
	VENTS(new VentsNoiseModifier());

	public NoiseModifier noiseModifier;

	NoiseModifiers(NoiseModifier noiseModifier) {
		this.noiseModifier = noiseModifier;
	}
}
