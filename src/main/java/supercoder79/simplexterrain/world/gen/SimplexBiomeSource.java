package supercoder79.simplexterrain.world.gen;

import com.google.common.collect.ImmutableList;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.RegistryLookupCodec;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.BuiltinBiomes;
import net.minecraft.world.biome.source.BiomeLayerSampler;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.VanillaLayeredBiomeSource;
import net.minecraft.world.gen.ChunkRandom;
import supercoder79.simplexterrain.SimplexTerrain;
import supercoder79.simplexterrain.api.BackingBiomeSource;
import supercoder79.simplexterrain.api.Heightmap;
import supercoder79.simplexterrain.noise.gradient.OpenSimplexNoise;
import supercoder79.simplexterrain.world.BiomeData;
import supercoder79.simplexterrain.world.biomelayers.SimplexBiomeLayers;
import supercoder79.simplexterrain.world.noisetype.NoiseType;
import supercoder79.simplexterrain.world.noisetype.NoiseTypeHolder;

public class SimplexBiomeSource extends BiomeSource implements BackingBiomeSource {
	public static final Codec<SimplexBiomeSource> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			RegistryLookupCodec.of(Registry.BIOME_KEY).forGetter(source -> source.biomeRegistry),
			Codec.LONG.fieldOf("seed").stable().forGetter(source -> source.seed))
			.apply(instance, instance.stable(SimplexBiomeSource::new)));

	private final BiomeLayerSampler backingSampler;

	private final Registry<Biome> biomeRegistry;
	private final long seed;

	private Heightmap heightmap = Heightmap.NONE;
	private Heightmap continent = Heightmap.NONE;

	public SimplexBiomeSource(Registry<Biome> biomeRegistry, long seed) {
		super(VanillaLayeredBiomeSource.BIOMES.stream().map((registryKey) -> () -> (Biome)biomeRegistry.getOrThrow(registryKey)));
		this.biomeRegistry = biomeRegistry;
		this.seed = seed;

		this.backingSampler =  SimplexBiomeLayers.build(biomeRegistry, seed);
		NoiseTypeHolder.initialize(new ChunkRandom(seed));
	}

	public void setHeightmap(Heightmap heightmap) {
		this.heightmap = heightmap;
	}

	public void setContinentHeightmap(Heightmap heightmap) {
		this.continent = heightmap;
	}

	@Override
	public Biome getBiomeForNoiseGen(int x, int y, int z) {
		if (heightmap == null) return BuiltinBiomes.PLAINS;
		Biome biome = getBiomeAt(x, z, heightmap.getBiomeData(x, z));
		return biome == null ? BuiltinBiomes.PLAINS : biome;
	}

	public Biome getBiomeAt(int x, int z, BiomeData data) {
		int height = data.getHeight();

		int continent = this.continent.getHeight(x << 2, z << 2);

		if (continent < SimplexTerrain.CONFIG.seaLevel) {
			return this.biomeRegistry.get(BiomeKeys.OCEAN);
		}

		RegistryKey<Biome> key = this.biomeRegistry.getKey(this.backingSampler.sample(this.biomeRegistry, x << 2, z << 2)).get();;

		NoiseType type = NoiseTypeHolder.get(key).get(x << 2, z << 2);

		// TODO: provide a better way of doing this
		int y = (int) type.modify(x << 2, z << 2, 0, 1, new BiomeData());

		return this.biomeRegistry.get(type.modify(y, key));
	}

	@Override
	protected Codec<? extends BiomeSource> getCodec() {
		return CODEC;
	}

	@Override
	public BiomeSource withSeed(long l) {
		return new SimplexBiomeSource(this.biomeRegistry, l);
	}

	@Override
	public RegistryKey<Biome> getBacking(int x, int z) {
		return this.biomeRegistry.getKey(this.backingSampler.sample(this.biomeRegistry, x, z)).get();
	}
}
