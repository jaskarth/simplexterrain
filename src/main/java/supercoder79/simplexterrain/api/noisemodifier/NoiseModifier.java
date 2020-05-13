package supercoder79.simplexterrain.api.noisemodifier;

import net.minecraft.world.gen.ChunkRandom;
import supercoder79.simplexterrain.api.noise.Noise;
import supercoder79.simplexterrain.api.noise.OctaveNoiseSampler;

public interface NoiseModifier {
	void init(long seed);
	void setup();
	double modify(int x, int z, double currentNoiseValue);
}
