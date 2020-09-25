package supercoder79.simplexterrain.configs;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import supercoder79.simplexterrain.SimplexTerrain;
import supercoder79.simplexterrain.api.noise.NoiseType;

public class ConfigHelper {
	public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	private static <T> Constructor<T> getConstructor(Class<T> clazz) {
		try {
			return clazz.getDeclaredConstructor();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static <T> T create(Constructor<T> constructor) {
		if (constructor == null) {
			return null;
		}

		try {
			return constructor.newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T> T getFromConfig(Class<T> configClass, Path path) {
		T config = create(getConstructor(configClass));
		try {
			//config exists: return value
			if (Files.exists(path)) {
				config = GSON.fromJson(new FileReader(path.toFile()), configClass);

				//update to newest config using le epic reflection hacks
				String version = (String) config.getClass().getField("version").get(config);
				if (!version.equals(SimplexTerrain.VERSION)) {
					config.getClass().getField("version").set(config, SimplexTerrain.VERSION);
					BufferedWriter writer = new BufferedWriter(new FileWriter(path.toFile()));
					writer.write(GSON.toJson(config));
					writer.close();
				}
			} else {
				//config does not exist: write value
				BufferedWriter writer = new BufferedWriter(new FileWriter(path.toFile()));
				writer.write(GSON.toJson(config));
				writer.close();
			}
		} catch (IOException | NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return config;
	}

	public static void init() {
		MainConfigData configData = null;
		try {

			Path configDir = Paths.get("", "config", "simplexterrain.json");
			if (Files.exists(configDir)) {
				configData = GSON.fromJson(new FileReader(configDir.toFile()), MainConfigData.class);
				//save new values
				if (!configData.configVersion.equals(SimplexTerrain.VERSION)) {
					configData.configVersion = SimplexTerrain.VERSION;
					BufferedWriter writer = new BufferedWriter(new FileWriter(configDir.toFile()));
					writer.write(GSON.toJson(configData));
					writer.close();
				}
			} else {
				configData = new MainConfigData();
				Paths.get("", "config").toFile().mkdirs();
				BufferedWriter writer = new BufferedWriter(new FileWriter(configDir.toFile()));
				writer.write(GSON.toJson(configData));

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
