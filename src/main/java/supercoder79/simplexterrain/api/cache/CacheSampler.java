package supercoder79.simplexterrain.api.cache;

import java.util.HashMap;

import net.minecraft.util.math.BlockPos;
import supercoder79.simplexterrain.SimplexTerrain;
import supercoder79.simplexterrain.api.noise.OctaveNoiseSampler;

/**
 * Samples from an octave noise sampler using caches. Only useful in circumstances where a single position is sampled multiple times.
 *
 * @author SuperCoder79
 */
public class CacheSampler {
	protected HashMap<Long, Double> cache = new HashMap<>();
	protected final OctaveNoiseSampler sampler;

	public CacheSampler(OctaveNoiseSampler sampler) {
		this.sampler = sampler;
	}

	public double sample(int x, int z) {
		//test the cache
		Double val = cache.get(BlockPos.asLong(x, 0, z));
		if (val != null) {
			return val;
		}

		//not in cache
		val = sampler.sample(x, z);
		cache.put(BlockPos.asLong(x, 0, z), val);
		return val;
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

	public static CacheSampler makeCacheSampler(OctaveNoiseSampler sampler) {
		return SimplexTerrain.CONFIG.optimizeForRamUsage ? new NoCacheSampler(sampler) : new CacheSampler(sampler);
	}
}
