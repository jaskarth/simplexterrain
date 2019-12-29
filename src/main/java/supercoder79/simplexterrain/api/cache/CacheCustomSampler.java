package supercoder79.simplexterrain.api.cache;

import net.minecraft.util.math.BlockPos;
import supercoder79.simplexterrain.api.noise.OctaveNoiseSampler;

/**
 * Samples using the OctaveNoiseSampler's sampleCustom function.
 *
 * @author SuperCoder79
 */
public class CacheCustomSampler extends CacheSampler {
	public CacheCustomSampler(OctaveNoiseSampler sampler) {
		super(sampler);
	}

	public double sampleCustom(int x, int z, double samplingFrequency, double amplitude, int octaves) {
		//test the cache
		Double val = cache.get(BlockPos.asLong(x, 0, z));
		if (val != null) {
			return val;
		}

		//not in cache
		val = sampler.sampleCustom(x, z, samplingFrequency, amplitude, amplitude, octaves);
		cache.put(BlockPos.asLong(x, 0, z), val);
		return val;
	}
}
