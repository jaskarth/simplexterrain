package supercoder79.simplexterrain.world.gen;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.source.BiomeLayerSampler;
import net.minecraft.world.biome.source.BiomeSource;
import supercoder79.simplexterrain.SimplexTerrain;
import supercoder79.simplexterrain.api.Heightmap;
import supercoder79.simplexterrain.noise.gradient.OpenSimplexNoise;
import supercoder79.simplexterrain.world.biomelayers.LandBiomeLayers;

public class SimplexBiomeSource extends BiomeSource {
	private final BiomeLayerSampler lowlandsSampler;
	private final BiomeLayerSampler midlandsSampler;
	private final BiomeLayerSampler highlandsSampler;
	private final BiomeLayerSampler toplandsSampler;
	private final BiomeLayerSampler beachSampler;
	private final BiomeLayerSampler oceanSampler;
	private final BiomeLayerSampler deepOceanSampler;

	private final OpenSimplexNoise beachStartSampler;

	private static final Set<Biome> biomes = ImmutableSet.of(Biomes.OCEAN, Biomes.PLAINS, Biomes.DESERT, Biomes.MOUNTAINS, Biomes.FOREST, Biomes.TAIGA, Biomes.SWAMP, Biomes.RIVER, Biomes.FROZEN_OCEAN, Biomes.FROZEN_RIVER, Biomes.SNOWY_TUNDRA, Biomes.SNOWY_MOUNTAINS, Biomes.MUSHROOM_FIELDS, Biomes.MUSHROOM_FIELD_SHORE, Biomes.BEACH, Biomes.DESERT_HILLS, Biomes.WOODED_HILLS, Biomes.TAIGA_HILLS, Biomes.MOUNTAIN_EDGE, Biomes.JUNGLE, Biomes.JUNGLE_HILLS, Biomes.JUNGLE_EDGE, Biomes.DEEP_OCEAN, Biomes.STONE_SHORE, Biomes.SNOWY_BEACH, Biomes.BIRCH_FOREST, Biomes.BIRCH_FOREST_HILLS, Biomes.DARK_FOREST, Biomes.SNOWY_TAIGA, Biomes.SNOWY_TAIGA_HILLS, Biomes.GIANT_TREE_TAIGA, Biomes.GIANT_TREE_TAIGA_HILLS, Biomes.WOODED_MOUNTAINS, Biomes.SAVANNA, Biomes.SAVANNA_PLATEAU, Biomes.BADLANDS, Biomes.WOODED_BADLANDS_PLATEAU, Biomes.BADLANDS_PLATEAU, Biomes.WARM_OCEAN, Biomes.LUKEWARM_OCEAN, Biomes.COLD_OCEAN, Biomes.DEEP_WARM_OCEAN, Biomes.DEEP_LUKEWARM_OCEAN, Biomes.DEEP_COLD_OCEAN, Biomes.DEEP_FROZEN_OCEAN, Biomes.SUNFLOWER_PLAINS, Biomes.DESERT_LAKES, Biomes.GRAVELLY_MOUNTAINS, Biomes.FLOWER_FOREST, Biomes.TAIGA_MOUNTAINS, Biomes.SWAMP_HILLS, Biomes.ICE_SPIKES, Biomes.MODIFIED_JUNGLE, Biomes.MODIFIED_JUNGLE_EDGE, Biomes.TALL_BIRCH_FOREST, Biomes.TALL_BIRCH_HILLS, Biomes.DARK_FOREST_HILLS, Biomes.SNOWY_TAIGA_MOUNTAINS, Biomes.GIANT_SPRUCE_TAIGA, Biomes.GIANT_SPRUCE_TAIGA_HILLS, Biomes.MODIFIED_GRAVELLY_MOUNTAINS, Biomes.SHATTERED_SAVANNA, Biomes.SHATTERED_SAVANNA_PLATEAU, Biomes.ERODED_BADLANDS, Biomes.MODIFIED_WOODED_BADLANDS_PLATEAU, Biomes.MODIFIED_BADLANDS_PLATEAU);

	private Heightmap heightmap = Heightmap.NONE;

	public SimplexBiomeSource(SimplexBiomeSourceConfig config) {
		super(biomes);
		long seed = config.getSeed();

		BiomeLayerSampler[] biomeLayerSamplers = LandBiomeLayers.build(seed, config.getGeneratorType());

		this.lowlandsSampler = biomeLayerSamplers[0];
		this.midlandsSampler = biomeLayerSamplers[1];
		this.highlandsSampler = biomeLayerSamplers[2];
		this.toplandsSampler = biomeLayerSamplers[3];
		this.beachSampler = biomeLayerSamplers[4];
		this.oceanSampler = biomeLayerSamplers[5];
		this.deepOceanSampler = biomeLayerSamplers[6];

		beachStartSampler = new OpenSimplexNoise(seed + 12);
	}

	public void setHeightmap(Heightmap heightmap) {
		this.heightmap = heightmap;
	}

	@Override
	public Biome getBiomeForNoiseGen(int x, int y, int z) {
		if (heightmap == null) return Biomes.OCEAN;

		Biome biome = sampleBiomeWithMathTM(x, z, heightmap.getHeight((x << 2), (z << 2)));
		return biome == null ? Biomes.OCEAN : biome;
	}

	public Biome sampleBiomeWithMathTM(int x, int z, int height) {
		Biome lowlands = this.lowlandsSampler.sample(x, z);
		if (height < SimplexTerrain.CONFIG.seaLevel / 2) return this.deepOceanSampler.sample(x, z);
		if (height < SimplexTerrain.CONFIG.seaLevel - 9) return this.oceanSampler.sample(x, z);
		if (height < SimplexTerrain.CONFIG.seaLevel - 4) return Biomes.OCEAN;
		if (height < SimplexTerrain.CONFIG.lowlandStartHeight + (beachStartSampler.sample(x / 128f, z / 128f)*6)) {
			if (lowlands == Biomes.BADLANDS) return lowlands;
			if (lowlands == Biomes.SWAMP) return lowlands;
			return this.beachSampler.sample(x, z);
		}
		if (height < SimplexTerrain.CONFIG.midlandStartHeight) return this.lowlandsSampler.sample(x, z);
		if (height < SimplexTerrain.CONFIG.highlandStartHeight) return this.midlandsSampler.sample(x, z);
		if (height < SimplexTerrain.CONFIG.toplandStartHeight) return this.highlandsSampler.sample(x, z);
		return this.toplandsSampler.sample(x, z);
	}
}
