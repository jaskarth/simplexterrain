package supercoder79.simplexterrain.impl;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.util.Identifier;
import net.minecraft.world.biome.layer.util.LayerRandomnessSource;

public class BiomePicker {
	private final List<Entry> biomeEntries = Lists.newArrayList();
	private double weightTotal;
	
	public Identifier pickBiome(LayerRandomnessSource random) {
		double randVal = target(random);
		
		int i = -1;
		while (randVal >= 0) {
			++i;
			randVal -= biomeEntries.get(i).weight;
		}
		
		return biomeEntries.get(i).biome;
	}
	
	public void addBiome(Identifier biome, double weight) {
		this.biomeEntries.add(new Entry(biome, weight));
	}
	
	private double target(LayerRandomnessSource random) {
		return (double) random.nextInt(Integer.MAX_VALUE) * weightTotal / Integer.MAX_VALUE;
	}
	
	public static class Entry {
		public Entry(Identifier biome, double weight) {
			this.biome = biome;
			this.weight = weight;
		}
		
		private final Identifier biome;
		private final double weight;
	}
}
