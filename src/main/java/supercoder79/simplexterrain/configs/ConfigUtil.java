package supercoder79.simplexterrain.configs;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import supercoder79.simplexterrain.SimplexTerrain;

public class ConfigUtil {
	public static Gson gson = new GsonBuilder().setPrettyPrinting().create();

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
				config = ConfigUtil.gson.fromJson(new FileReader(path.toFile()), configClass);

				//update to newest config using le epic reflection hacks
				String version = (String) config.getClass().getField("version").get(config);
				if (!version.equals(SimplexTerrain.VERSION)) {
					config.getClass().getField("version").set(config, SimplexTerrain.VERSION);
					BufferedWriter writer = new BufferedWriter(new FileWriter(path.toFile()));
					writer.write(ConfigUtil.gson.toJson(config));
					writer.close();
				}
			} else {
				//config does not exist: write value
				BufferedWriter writer = new BufferedWriter(new FileWriter(path.toFile()));
				writer.write(ConfigUtil.gson.toJson(config));
				writer.close();
			}
		} catch (IOException | NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return config;
	}
}
