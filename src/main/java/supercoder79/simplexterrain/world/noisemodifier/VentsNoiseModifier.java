package supercoder79.simplexterrain.world.noisemodifier;

import net.minecraft.world.gen.ChunkRandom;
import supercoder79.simplexterrain.SimplexTerrain;
import supercoder79.simplexterrain.api.noise.NoiseModifier;
import supercoder79.simplexterrain.api.noise.OctaveNoiseSampler;
import supercoder79.simplexterrain.noise.gradient.OpenSimplexNoise;

public class VentsNoiseModifier extends NoiseModifier {
	private ChunkRandom random = new ChunkRandom(0);
	private OpenSimplexNoise noiseSampler;

	public VentsNoiseModifier() {
		super(4L);
	}

	@Override
	public void init(long seed) {
		random.setSeed(seed);
		noiseSampler = new OpenSimplexNoise(seed - 56);
	}

	@Override
	public void setup() {

	}

	@Override
	public double modify(int x, int z, double currentNoiseValue, double scaleValue) {
		double noise = noiseSampler.sample((double) x / 295, (double) z / 295);

		double dist = Math.abs(noise - 0.7);

		if (dist <= 0.0125) {
			currentNoiseValue += ((dist*500) + 40);
		} else if (noise > 0.7) {
			currentNoiseValue -= noise*60;
		}
		return currentNoiseValue;
	}
}
