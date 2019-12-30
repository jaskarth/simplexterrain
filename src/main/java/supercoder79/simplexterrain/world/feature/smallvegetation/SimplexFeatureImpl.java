package supercoder79.simplexterrain.world.feature.smallvegetation;

import supercoder79.simplexterrain.api.feature.FeaturePack;

import java.util.ArrayList;
import java.util.List;

public class SimplexFeatureImpl {
	private static List<FeaturePack> featurePacks = new ArrayList<>();

	public static void addFeaturePack(FeaturePack fp) {
		featurePacks.add(fp);
	}

	public static List<FeaturePack> getFeaturePacks() {
		return featurePacks;
	}
}
