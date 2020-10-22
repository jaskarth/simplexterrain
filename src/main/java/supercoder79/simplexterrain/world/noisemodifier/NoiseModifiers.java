package supercoder79.simplexterrain.world.noisemodifier;

import supercoder79.simplexterrain.api.noisemodifier.NoiseModifier;

public enum NoiseModifiers {
	MOUNTAINS(new MountainsNoiseModifier()),
	RIDGES(new RidgesNoiseModifier()),
	DETAILS(new DetailNoiseModifier()),
	PLATEAUS(new PlateausNoiseModifier()),
	RIVERS(new RiversNoiseModifier());

	public NoiseModifier noiseModifier;

	NoiseModifiers(NoiseModifier noiseModifier) {
		this.noiseModifier = noiseModifier;
	}
}
