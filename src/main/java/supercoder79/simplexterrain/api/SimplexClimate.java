package supercoder79.simplexterrain.api;

import supercoder79.simplexterrain.world.biomelayers.layers.SimplexClimateLayer;

public enum SimplexClimate {
	LUSH_TROPICAL(0),
	TROPICAL(1),
	DRY_TROPICAL(2),
	LUSH_TEMPERATE(3),
	TEMPERATE(4),
	DRY_TEMPERATE(5),
	LUSH_BOREAL(6),
	BOREAL(7),
	DRY_BOREAL(8),
	SNOWY(9);

	private SimplexClimate(int id) {
		this.id = id;
		SimplexClimateLayer.REVERSE_ID_MAP[id] = this;
	}

	public final int id;

	public static SimplexClimate fromTemperatureHumidity(double temp, double humidity) {
		if (temp < -0.58) {
			return SNOWY;
		} else if (temp < -0.28) {
			if (humidity < -0.3) {
				return DRY_BOREAL;
			} else if (humidity > 0.3) {
				return LUSH_BOREAL;
			} else {
				return BOREAL;
			}
		} else if (temp > 0.28) {
			if (humidity < -0.3) {
				return DRY_TROPICAL;
			} else if (humidity > 0.3) {
				return LUSH_TROPICAL;
			} else {
				return TROPICAL;
			}
		} else {
			if (humidity < -0.3) {
				return DRY_TEMPERATE;
			} else if (humidity > 0.3) {
				return LUSH_TEMPERATE;
			} else {
				return TEMPERATE;
			}
		}
	}
}
