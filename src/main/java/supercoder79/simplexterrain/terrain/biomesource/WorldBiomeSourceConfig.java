package supercoder79.simplexterrain.terrain.biomesource;

import net.minecraft.world.biome.source.BiomeSourceConfig;
import net.minecraft.world.gen.chunk.OverworldChunkGeneratorConfig;
import net.minecraft.world.level.LevelGeneratorType;
import net.minecraft.world.level.LevelProperties;

public class WorldBiomeSourceConfig implements BiomeSourceConfig {
	private final long seed;
	private final LevelGeneratorType generatorType;
	private OverworldChunkGeneratorConfig generatorSettings = new OverworldChunkGeneratorConfig();

	public WorldBiomeSourceConfig(Object levelProperties) {
		this.seed = ((LevelProperties)(levelProperties)).getSeed();
		this.generatorType = ((LevelProperties)(levelProperties)).getGeneratorType();
	}

	public WorldBiomeSourceConfig setGeneratorSettings(OverworldChunkGeneratorConfig overworldChunkGeneratorConfig) {
		this.generatorSettings = overworldChunkGeneratorConfig;
		return this;
	}

	public OverworldChunkGeneratorConfig getGeneratorSettings() {
		return this.generatorSettings;
	}

	public long getSeed() {
		return seed;
	}

	public LevelGeneratorType getGeneratorType() {
		return generatorType;
	}
}
