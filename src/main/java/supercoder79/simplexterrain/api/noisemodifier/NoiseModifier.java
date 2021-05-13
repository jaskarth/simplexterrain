package supercoder79.simplexterrain.api.noisemodifier;

import net.minecraft.world.gen.ChunkRandom;
import supercoder79.simplexterrain.api.noise.Noise;
import supercoder79.simplexterrain.api.noise.OctaveNoiseSampler;
import supercoder79.simplexterrain.world.BiomeData;

public interface NoiseModifier {
	void init(long seed);
	void setup();
	double modify(int x, int z, double currentNoiseValue, BiomeData data);
}
