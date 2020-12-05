package supercoder79.simplexterrain.world.gen;

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
import supercoder79.simplexterrain.SimplexTerrain;
import supercoder79.simplexterrain.api.Heightmap;
import supercoder79.simplexterrain.mixin.BiomeLayerSamplerAccessor;
import supercoder79.simplexterrain.noise.gradient.OpenSimplexNoise;
import supercoder79.simplexterrain.world.BiomeData;
import supercoder79.simplexterrain.world.biomelayers.SimplexBiomeLayers;

public class SimplexBiomeSource extends BiomeSource {
	public static final Codec<SimplexBiomeSource> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			RegistryLookupCodec.of(Registry.BIOME_KEY).forGetter(source -> source.biomeRegistry),
			Codec.LONG.fieldOf("seed").stable().forGetter(source -> source.seed))
			.apply(instance, instance.stable(SimplexBiomeSource::new)));
	private final BiomeLayerSampler lowlandsSampler;
	private final BiomeLayerSampler midlandsSampler;
	private final BiomeLayerSampler highlandsSampler;
	private final BiomeLayerSampler toplandsSampler;
	private final BiomeLayerSampler beachSampler;
	private final BiomeLayerSampler oceanSampler;
	private final BiomeLayerSampler deepOceanSampler;

	private final OpenSimplexNoise beachStartSampler;

	private final Registry<Biome> biomeRegistry;
	private final long seed;

	private Heightmap heightmap = Heightmap.NONE;

	public SimplexBiomeSource(Registry<Biome> biomeRegistry, long seed) {
		super(VanillaLayeredBiomeSource.BIOMES.stream().map((registryKey) -> () -> (Biome)biomeRegistry.getOrThrow(registryKey)));
		this.biomeRegistry = biomeRegistry;
		this.seed = seed;

		BiomeLayerSampler[] biomeLayerSamplers = SimplexBiomeLayers.build(biomeRegistry, seed);

		this.lowlandsSampler = biomeLayerSamplers[0];
		this.midlandsSampler = biomeLayerSamplers[1];
		this.highlandsSampler = biomeLayerSamplers[2];
		this.toplandsSampler = biomeLayerSamplers[3];
		this.beachSampler = biomeLayerSamplers[4];
		this.oceanSampler = biomeLayerSamplers[5];
		this.deepOceanSampler = biomeLayerSamplers[6];

		this.beachStartSampler = new OpenSimplexNoise(seed + 12);
	}

	public void setHeightmap(Heightmap heightmap) {
		this.heightmap = heightmap;
	}

	@Override
	public Biome getBiomeForNoiseGen(int x, int y, int z) {
		if (heightmap == null) return BuiltinBiomes.PLAINS;
		Biome biome = getBiomeAt(x, z, heightmap.getBiomeData(x << 2, z << 2));
		return biome == null ? BuiltinBiomes.PLAINS : biome;
	}

	public Biome getBiomeAt(int x, int z, BiomeData data) {
		int height = data.getHeight();

		Biome lowlands = sample(this.lowlandsSampler, x, z);
		boolean isSwamp = this.biomeRegistry.getId(lowlands) == BiomeKeys.SWAMP.getValue();

		if (data.isRiver()) {
			if (isSwamp) {
				return biomeRegistry.get(BiomeKeys.SWAMP);
			}

			if (lowlands.getPrecipitation() == Biome.Precipitation.SNOW) {
				return biomeRegistry.get(BiomeKeys.FROZEN_RIVER);
			}

			return biomeRegistry.get(BiomeKeys.RIVER);
		}

		if (data.isForcedLowlands()) {
			return lowlands;
		}

		if (height < SimplexTerrain.CONFIG.seaLevel - 20) return sample(this.deepOceanSampler, x, z);
		if (height < SimplexTerrain.CONFIG.seaLevel - 4) return sample(this.oceanSampler, x, z);
		if (height < SimplexTerrain.CONFIG.lowlandStartHeight + (beachStartSampler.sample(x / 128.0, z / 128.0) * 4)) {
			//TODO: unhardcode
			if (this.biomeRegistry.getId(lowlands) == BiomeKeys.BADLANDS.getValue()) return lowlands;
			if (this.biomeRegistry.getId(lowlands) == BiomeKeys.BADLANDS_PLATEAU.getValue()) return lowlands;
			if (isSwamp) return lowlands;
			return sample(this.beachSampler, x, z);
		}

		if (height < SimplexTerrain.CONFIG.midlandStartHeight) return sample(this.lowlandsSampler, x, z);
		if (height < SimplexTerrain.CONFIG.highlandStartHeight) return sample(this.midlandsSampler, x, z);
		if (height < SimplexTerrain.CONFIG.toplandStartHeight) return sample(this.highlandsSampler, x, z);
		return sample(this.toplandsSampler, x, z);
	}

	private Biome sample(BiomeLayerSampler sampler, int x, int z) {
		int id = ((BiomeLayerSamplerAccessor)sampler).getSampler().sample(x, z);
		RegistryKey<Biome> registryKey = BuiltinBiomes.fromRawId(id);
		if (registryKey == null) {
			return this.biomeRegistry.get(BuiltinBiomes.fromRawId(0));
		} else {
			Biome biome = this.biomeRegistry.get(registryKey);
			if (biome == null) {
				return this.biomeRegistry.get(BuiltinBiomes.fromRawId(0));
			} else {
				return biome;
			}
		}
	}

	@Override
	protected Codec<? extends BiomeSource> getCodec() {
		return CODEC;
	}

	@Override
	public BiomeSource withSeed(long l) {
		return new SimplexBiomeSource(this.biomeRegistry, l);
	}
}
