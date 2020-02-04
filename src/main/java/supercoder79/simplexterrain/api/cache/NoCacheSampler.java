package supercoder79.simplexterrain.api.cache;

import supercoder79.simplexterrain.api.noise.OctaveNoiseSampler;

/**
 * CacheSampler implementation without caching for less ram usage.
 *
 * @author SuperCoder79
 */
public class NoCacheSampler extends CacheSampler {
	public NoCacheSampler(OctaveNoiseSampler sampler) {
		super(sampler);
	}

	public double sample(int x, int z) {
		return sampler.sample(x, z);
	}

	public double sampleCustom(int x, int z, double samplingFrequency, double amplitude, int octaves) {
		return sampler.sampleCustom(x, z, samplingFrequency, amplitude, amplitude, octaves);
	}
}
