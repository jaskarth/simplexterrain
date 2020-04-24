package supercoder79.simplexterrain.world.gen;

import java.util.function.Supplier;

import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.chunk.ChunkGeneratorType;
import net.minecraft.world.gen.chunk.OverworldChunkGeneratorConfig;

public class WorldGeneratorType extends ChunkGeneratorType<OverworldChunkGeneratorConfig, SimplexChunkGenerator> {

	public WorldGeneratorType(boolean buffetScreen, Supplier<OverworldChunkGeneratorConfig> configSupplier) {
		super(null, buffetScreen, configSupplier);
	}

	public static void init() {
		// NO-OP
	}

	@Override
	public SimplexChunkGenerator create(IWorld world, BiomeSource biomeSource, OverworldChunkGeneratorConfig config) {
		return new SimplexChunkGenerator(world, biomeSource, config);
	}
}