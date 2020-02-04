package supercoder79.simplexterrain.api.cache;

import supercoder79.simplexterrain.api.noise.OctaveNoiseSampler;

/**
 * AbstractSampler implementation without caching for less ram usage.
 *
 * @author SuperCoder79
 */
public class DefaultSampler extends AbstractSampler {
	public DefaultSampler(OctaveNoiseSampler sampler) {
		super(sampler);
	}

	@Override
	public double sample(int x, int z) {
		return sampler.sample(x, z);
	}

	@Override
	public double sampleCustom(int x, int z, double samplingFrequency, double amplitude, int octaves) {
		return sampler.sampleCustom(x, z, samplingFrequency, amplitude, amplitude, octaves);
	}
}
