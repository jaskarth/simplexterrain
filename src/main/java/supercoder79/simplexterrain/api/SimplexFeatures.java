package supercoder79.simplexterrain.api;

import supercoder79.simplexterrain.api.feature.FeaturePack;
import supercoder79.simplexterrain.world.feature.smallvegetation.SimplexFeatureImpl;

/**
 * API for registering new feature packs.
 *
 * @author SuperCoder79
 */
public final class SimplexFeatures {
	private SimplexFeatures() {
	}

	public static void addFeaturePack(FeaturePack fp) {
		SimplexFeatureImpl.addFeaturePack(fp);
	}
}
