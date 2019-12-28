package supercoder79.simplexterrain.world.biome;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.layer.util.LayerRandomnessSource;

/**
 * Picks a biome. Pretty self explanatory.
 * 
 * @author Valoeghese
 */
public class BiomePicker {
	private final List<Entry> biomeEntries = Lists.newArrayList();
	private double weightTotal;

	public Biome pickBiome(LayerRandomnessSource rand) {
		double randVal = target(rand);
		int i = -1;

		while (randVal >= 0) {
			++i;
			randVal -= biomeEntries.get(i).weight;
		}
		return biomeEntries.get(i).getBiome();
	}

	public void addBiome(Identifier biome, double weight) {
		this.biomeEntries.add(new Entry(biome, weight));
		weightTotal += weight;
	}

	private double target(LayerRandomnessSource random) {
		return (double) random.nextInt(Integer.MAX_VALUE) * weightTotal / Integer.MAX_VALUE;
	}

	public static class Entry {
		public Entry(Identifier biome, double weight) {
			this.biomeId = biome;
			this.weight = weight;
		}
		
		private Biome getBiome() {
			return biome == null ? calculateBiome() : biome;
		}
		
		private Biome calculateBiome() {
			biome = Registry.BIOME.get(biomeId);
			return biome;
		}

		private Biome biome = null;
		private final Identifier biomeId;
		private final double weight;
	}
}
