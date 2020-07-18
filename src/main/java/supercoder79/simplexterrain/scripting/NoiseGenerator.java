package supercoder79.simplexterrain.scripting;

import java.util.Locale;

import supercoder79.simplexterrain.api.noise.NoiseType;
import supercoder79.simplexterrain.api.noise.OctaveNoiseSampler;

public class NoiseGenerator extends OctaveNoiseSampler {
	public NoiseGenerator(String noiseType, int octaves, double frequency, double amplitudeHigh, double amplitudeLow) {
		super(
				NoiseType.valueOf(noiseType.toUpperCase(Locale.ROOT)).noiseClass,
				SimplexScripting.randomCache,
				octaves,
				frequency,
				amplitudeHigh,
				amplitudeLow);
	}
}
