package supercoder79.simplexterrain.terrain;

import com.google.common.collect.Sets;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.noise.OctaveSimplexNoiseSampler;
import net.minecraft.util.math.noise.SimplexNoiseSampler;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.layer.BiomeLayerSampler;
import net.minecraft.world.biome.layer.BiomeLayers;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.VanillaLayeredBiomeSourceConfig;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.chunk.OverworldChunkGeneratorConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.level.LevelProperties;
import supercoder79.simplexterrain.api.Heightmap;

import java.util.*;

public class WorldBiomeSource extends BiomeSource {
    private final BiomeLayerSampler noiseLayer;
    private final BiomeLayerSampler biomeLayer;
    private OctaveSimplexNoiseSampler lowlandsLayer;
    private OctaveSimplexNoiseSampler lowlandsLayer2;
    private OctaveSimplexNoiseSampler midlandsLayer;
    private OctaveSimplexNoiseSampler highlandsLayer;
    private OctaveSimplexNoiseSampler oceanTemperatureLayer;
    private final Biome[] biomes;

    private Heightmap heightmap = Heightmap.NONE;

    public WorldBiomeSource(Object o) {
        VanillaLayeredBiomeSourceConfig config = (VanillaLayeredBiomeSourceConfig)o;
        this.biomes = new Biome[]{Biomes.OCEAN, Biomes.PLAINS, Biomes.DESERT, Biomes.MOUNTAINS, Biomes.FOREST, Biomes.TAIGA, Biomes.SWAMP, Biomes.RIVER, Biomes.FROZEN_OCEAN, Biomes.FROZEN_RIVER, Biomes.SNOWY_TUNDRA, Biomes.SNOWY_MOUNTAINS, Biomes.MUSHROOM_FIELDS, Biomes.MUSHROOM_FIELD_SHORE, Biomes.BEACH, Biomes.DESERT_HILLS, Biomes.WOODED_HILLS, Biomes.TAIGA_HILLS, Biomes.MOUNTAIN_EDGE, Biomes.JUNGLE, Biomes.JUNGLE_HILLS, Biomes.JUNGLE_EDGE, Biomes.DEEP_OCEAN, Biomes.STONE_SHORE, Biomes.SNOWY_BEACH, Biomes.BIRCH_FOREST, Biomes.BIRCH_FOREST_HILLS, Biomes.DARK_FOREST, Biomes.SNOWY_TAIGA, Biomes.SNOWY_TAIGA_HILLS, Biomes.GIANT_TREE_TAIGA, Biomes.GIANT_TREE_TAIGA_HILLS, Biomes.WOODED_MOUNTAINS, Biomes.SAVANNA, Biomes.SAVANNA_PLATEAU, Biomes.BADLANDS, Biomes.WOODED_BADLANDS_PLATEAU, Biomes.BADLANDS_PLATEAU, Biomes.WARM_OCEAN, Biomes.LUKEWARM_OCEAN, Biomes.COLD_OCEAN, Biomes.DEEP_WARM_OCEAN, Biomes.DEEP_LUKEWARM_OCEAN, Biomes.DEEP_COLD_OCEAN, Biomes.DEEP_FROZEN_OCEAN, Biomes.SUNFLOWER_PLAINS, Biomes.DESERT_LAKES, Biomes.GRAVELLY_MOUNTAINS, Biomes.FLOWER_FOREST, Biomes.TAIGA_MOUNTAINS, Biomes.SWAMP_HILLS, Biomes.ICE_SPIKES, Biomes.MODIFIED_JUNGLE, Biomes.MODIFIED_JUNGLE_EDGE, Biomes.TALL_BIRCH_FOREST, Biomes.TALL_BIRCH_HILLS, Biomes.DARK_FOREST_HILLS, Biomes.SNOWY_TAIGA_MOUNTAINS, Biomes.GIANT_SPRUCE_TAIGA, Biomes.GIANT_SPRUCE_TAIGA_HILLS, Biomes.MODIFIED_GRAVELLY_MOUNTAINS, Biomes.SHATTERED_SAVANNA, Biomes.SHATTERED_SAVANNA_PLATEAU, Biomes.ERODED_BADLANDS, Biomes.MODIFIED_WOODED_BADLANDS_PLATEAU, Biomes.MODIFIED_BADLANDS_PLATEAU};
        LevelProperties levelProperties = config.getLevelProperties();
        OverworldChunkGeneratorConfig overworldChunkGeneratorConfig = config.getGeneratorSettings();
        BiomeLayerSampler[] biomeLayerSamplers = WorldBiomeLayers.build(levelProperties.getSeed(), levelProperties.getGeneratorType(), overworldChunkGeneratorConfig);
        this.noiseLayer = biomeLayerSamplers[0];
        this.biomeLayer = biomeLayerSamplers[1];
        lowlandsLayer = new OctaveSimplexNoiseSampler(new ChunkRandom(levelProperties.getSeed()), 10);
        lowlandsLayer2 = new OctaveSimplexNoiseSampler(new ChunkRandom(levelProperties.getSeed()), 10);
        midlandsLayer = new OctaveSimplexNoiseSampler(new ChunkRandom(levelProperties.getSeed()), 10);
        highlandsLayer = new OctaveSimplexNoiseSampler(new ChunkRandom(levelProperties.getSeed()), 12);
        oceanTemperatureLayer = new OctaveSimplexNoiseSampler(new ChunkRandom(levelProperties.getSeed()), 10);
    }

    public void setHeightmap(Heightmap heightmap) {
        this.heightmap = heightmap;
    }

    @Override
    public Biome getBiome(int x, int z) {
        return sampleBiomeWithMathTM(x, z);
    }

    @Override
    public Biome[] sampleBiomes(int x, int z, int width, int height, boolean bl) {
        Biome[] biomes = new Biome[width * height];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                biomes[j + (i * width)] = sampleBiomeWithMathTM(x+j, z+i);
            }
        }
        return biomes;
    }

    private Biome sampleBiomeWithMathTM(int x, int z) {
        int height = heightmap.getHeight(x, z);
//        System.out.println(lowlandsLayer.sample(x, z)*0.02);
        if (height < 30) {
            if (oceanTemperatureLayer.sample(x, z)*0.005 > 2.5)
                return Biomes.DEEP_WARM_OCEAN;
            else if (oceanTemperatureLayer.sample(x, z)*0.005 > 1.0)
                return Biomes.DEEP_LUKEWARM_OCEAN;
            else if (oceanTemperatureLayer.sample(x, z)*0.005 < -2.5)
                return Biomes.DEEP_FROZEN_OCEAN;
            else if (oceanTemperatureLayer.sample(x, z)*0.005 < -1.0)
                return Biomes.DEEP_COLD_OCEAN;

            else return Biomes.DEEP_OCEAN;
        }
        if (height < 61) {
            if (oceanTemperatureLayer.sample(x, z)*0.005 > 2.5)
                return Biomes.WARM_OCEAN;
            else if (oceanTemperatureLayer.sample(x, z)*0.005 > 1.0)
                return Biomes.LUKEWARM_OCEAN;
            else if (oceanTemperatureLayer.sample(x, z)*0.005 < -2.5)
                return Biomes.FROZEN_OCEAN;
            else if (oceanTemperatureLayer.sample(x, z)*0.005 < -1.0)
                return Biomes.COLD_OCEAN;

            else return Biomes.OCEAN;
        }
        if (height < 66) return Biomes.BEACH;
        if (height < 90) {
            if (lowlandsLayer.sample(x, z)*0.01 > 4)
                return Biomes.SWAMP;
            else if (lowlandsLayer.sample(x, z)*0.01 > 3)
                return Biomes.DESERT;
            else if (lowlandsLayer.sample(x, z)*0.01 < -4)
                return Biomes.SAVANNA;
            else if (lowlandsLayer.sample(z, x)*0.01 < -3)
                return Biomes.JUNGLE;
            else if (lowlandsLayer.sample(z, x)*0.01 < -2.9)
                return Biomes.JUNGLE_EDGE;
            else
                return Biomes.PLAINS;
        }
        if (height < 140) {
            if (midlandsLayer.sample(x, z)*0.03 > 2.0) {
                if (midlandsLayer.sample(x, z)*0.03 > 9.0)
                    return Biomes.TALL_BIRCH_FOREST;
                return Biomes.BIRCH_FOREST;
            }
            else if (midlandsLayer.sample(z, x)*0.03 > 2.0)
                return Biomes.DARK_FOREST;
            else
                return Biomes.FOREST;
        }
        if (height < 190) return Biomes.TAIGA;
        return Biomes.MOUNTAINS;
    }

    @Override
    public Set<Biome> getBiomesInArea(int x, int z, int radius) {
        int i = x - radius >> 2;
        int j = z - radius >> 2;
        int k = x + radius >> 2;
        int l = z + radius >> 2;
        int m = k - i + 1;
        int n = l - j + 1;
        Set<Biome> set = Sets.newHashSet();
        Collections.addAll(set, this.sampleBiomes(i, j, m, n));
        return set;
    }
    @Override
    public BlockPos locateBiome(int x, int z, int radius, List<Biome> list, Random random) {
        int i = x - radius >> 2;
        int j = z - radius >> 2;
        int k = x + radius >> 2;
        int l = z + radius >> 2;
        int m = k - i + 1;
        int n = l - j + 1;
        Biome[] biomes2 = this.sampleBiomes(i, j, m, n);
        BlockPos blockPos = null;
        int o = 0;

        for(int p = 0; p < m * n; ++p) {
            int q = i + p % m << 2;
            int r = j + p / m << 2;
            if (list.contains(biomes2[p])) {
                if (blockPos == null || random.nextInt(o + 1) == 0) {
                    blockPos = new BlockPos(q, 0, r);
                }

                ++o;
            }
        }

        return blockPos;
    }

    @Override
    public boolean hasStructureFeature(StructureFeature<?> feature) {
        return (Boolean)this.structureFeatures.computeIfAbsent(feature, (structureFeature) -> {
            Biome[] var2 = this.biomes;
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                Biome biome = var2[var4];
                if (biome.hasStructureFeature(structureFeature)) {
                    return true;
                }
            }

            return false;
        });
    }

    @Override
    public Set<BlockState> getTopMaterials() {
        if (this.topMaterials.isEmpty()) {
            Biome[] var1 = this.biomes;
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                Biome biome = var1[var3];
                this.topMaterials.add(biome.getSurfaceConfig().getTopMaterial());
            }
        }

        return this.topMaterials;
    }
}
