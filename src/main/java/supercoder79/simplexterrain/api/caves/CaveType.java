package supercoder79.simplexterrain.api.caves;

import net.minecraft.world.gen.ProbabilityConfig;
import net.minecraft.world.gen.carver.Carver;
import net.minecraft.world.gen.carver.CarverConfig;
import supercoder79.simplexterrain.world.feature.Carvers;

public enum CaveType {
	VANILLA(Carver.CAVE, new ProbabilityConfig(0.14285715F)),
	RAVINES(Carver.CANYON, new ProbabilityConfig(0.02F)),
	SIMPLEX(Carvers.SIMPLEX_CARVER, new ProbabilityConfig(0.4F)),
	GRAVELLY(null, null);

	public Carver carver;
	public CarverConfig config;

	CaveType(Carver carver, CarverConfig config) {
		this.carver = carver;
		this.config = config;
	}
}
