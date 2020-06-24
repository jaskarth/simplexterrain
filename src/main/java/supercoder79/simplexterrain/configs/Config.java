package supercoder79.simplexterrain.configs;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import supercoder79.simplexterrain.SimplexTerrain;
import supercoder79.simplexterrain.api.noise.NoiseType;

public class Config {
	public static void init() {
		MainConfigData configData = null;
		try {

			Path configDir = Paths.get("", "config", "simplexterrain.json");
			if (Files.exists(configDir)) {
				configData = ConfigUtil.gson.fromJson(new FileReader(configDir.toFile()), MainConfigData.class);
				//save new values
				if (!configData.configVersion.equals(SimplexTerrain.VERSION)) {
					configData.configVersion = SimplexTerrain.VERSION;
					BufferedWriter writer = new BufferedWriter(new FileWriter(configDir.toFile()));
					writer.write(ConfigUtil.gson.toJson(configData));
					writer.close();
				}
			} else {
				configData = new MainConfigData();
				Paths.get("", "config").toFile().mkdirs();
				BufferedWriter writer = new BufferedWriter(new FileWriter(configDir.toFile()));
				writer.write(ConfigUtil.gson.toJson(configData));

				writer.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (configData.noiseGenerator == null) {
			System.out.println("[Simplex Terrain] The noise generator was null! Falling back to Simplex!");
			configData.noiseGenerator = NoiseType.SIMPLEX;
		}

		SimplexTerrain.CONFIG = configData;

		Paths.get("", "config", "simplexterrain", "noisemodifiers").toFile().mkdirs();
		Paths.get("", "config", "simplexterrain", "postprocessors").toFile().mkdirs();

		// Setup (reading from configs and stuff like that)
		SimplexTerrain.CONFIG.postProcessors.forEach(postProcessors -> postProcessors.postProcessor.setup());
		SimplexTerrain.CONFIG.noiseModifiers.forEach(noiseModifiers -> noiseModifiers.noiseModifier.setup());
	}
}
