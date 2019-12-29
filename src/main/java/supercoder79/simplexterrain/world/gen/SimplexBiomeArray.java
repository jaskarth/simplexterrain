package supercoder79.simplexterrain.world.gen;

import java.util.concurrent.CompletableFuture;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeArray;
import net.minecraft.world.biome.source.BiomeSource;
import supercoder79.simplexterrain.SimplexTerrain;


public class SimplexBiomeArray {
	private static final int HORIZONTAL_SECTION_COUNT = (int)Math.round(Math.log(16.0D) / Math.log(2.0D)) - 2;

	public static BiomeArray makeNewBiomeArray(ChunkPos pos, BiomeSource source) {
		Biome[] data = new Biome[BiomeArray.DEFAULT_LENGTH];

		CompletableFuture[] futures = new CompletableFuture[SimplexTerrain.CONFIG.noiseGenerationThreads];
		for (int i = 0; i < SimplexTerrain.CONFIG.noiseGenerationThreads; i++) {
			int finalI = i;
			futures[i] = CompletableFuture.runAsync(() -> generateBiomes(data, source, pos.getStartX() >> 2, pos.getStartZ() >> 2,
					finalI * BiomeArray.DEFAULT_LENGTH / SimplexTerrain.CONFIG.noiseGenerationThreads,
					BiomeArray.DEFAULT_LENGTH / SimplexTerrain.CONFIG.noiseGenerationThreads));
		}

		for (int i = 0; i < futures.length; i++) {
			futures[i].join();
		}

		return new BiomeArray(data);
	}

	private static void generateBiomes(Biome[] array, BiomeSource source, int x, int z, int start, int size) {
		for(int k = start; k < size; ++k) {
			int l = k & BiomeArray.HORIZONTAL_BIT_MASK;
			int m = k >> HORIZONTAL_SECTION_COUNT + HORIZONTAL_SECTION_COUNT & BiomeArray.VERTICAL_BIT_MASK;
			int n = k >> HORIZONTAL_SECTION_COUNT & BiomeArray.HORIZONTAL_BIT_MASK;
			array[k] = source.getBiomeForNoiseGen(x + l, m, z + n);
		}
	}
}
