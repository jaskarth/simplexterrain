package supercoder79.simplexterrain.world.feature;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.ProbabilityConfig;
import supercoder79.simplexterrain.SimplexTerrain;

public class Carvers {
	public static SimplexCarver SIMPLEX_CARVER;

	public static void init() {
		SIMPLEX_CARVER = Registry.register(Registry.CARVER, new Identifier("simplexterrain", "simplex_carver"), new SimplexCarver(ProbabilityConfig::deserialize));
	}
}
