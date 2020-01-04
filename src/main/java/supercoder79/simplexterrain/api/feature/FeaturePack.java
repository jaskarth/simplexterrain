package supercoder79.simplexterrain.api.feature;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A holder for multiple SimplexFeatures, and specifies what features are overriden.
 *
 * @author SuperCoder79
 */
public abstract class FeaturePack {

	public abstract List<SimplexFeature> featuresInPack();

	public Map<GenerationStep.Feature, Set<Biome>> overridenFeatures() {
		return new HashMap<>();
	}
}
