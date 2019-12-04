package supercoder79.simplexterrain.api.biomes;

import supercoder79.simplexterrain.world.biomelayers.layers.SimplexClimateLayer;

/**
 * Specifies the climates used to determine the biomes.
 * It's basically just a couple of if statements with values fed in from simplex noise.
 *
 * @author Valoeghese
 */
public enum SimplexClimate {
	//lush: humidity > 0.3
	//dry: humidity < -0.3
	//normal: between -0.3 and 0.3

	//temp above 0.28
	LUSH_TROPICAL(0),
	TROPICAL(1),
	DRY_TROPICAL(2),
	//temp between -0.58 and 0.28
	LUSH_TEMPERATE(3),
	TEMPERATE(4),
	DRY_TEMPERATE(5),
	//temp between -0.58 and -0.28
	LUSH_BOREAL(6),
	BOREAL(7),
	DRY_BOREAL(8),
	// temp less than -0.58
	SNOWY(9);

	SimplexClimate(int id) {
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
