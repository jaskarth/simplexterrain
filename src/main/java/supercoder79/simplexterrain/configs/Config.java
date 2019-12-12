package supercoder79.simplexterrain.configs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import supercoder79.simplexterrain.SimplexTerrain;
import supercoder79.simplexterrain.api.noise.NoiseType;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Config {
    public static ConfigData init() {
        ConfigData configData = null;
        try {
            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting();
            Gson gson = builder.create();
            Path configDir = Paths.get("", "config", "simplexterrain.json");
            if (Files.exists(configDir)) {
                configData = gson.fromJson(new FileReader(configDir.toFile()), ConfigData.class);
                //save new values
                if (!configData.configVersion.equals(SimplexTerrain.VERSION)) {
                    configData.configVersion = SimplexTerrain.VERSION;
                    BufferedWriter writer = new BufferedWriter(new FileWriter(configDir.toFile()));
                    writer.write(gson.toJson(configData));

                    writer.close();
                }
            } else {
                configData = new ConfigData();
                Paths.get("", "config").toFile().mkdirs();
                BufferedWriter writer = new BufferedWriter(new FileWriter(configDir.toFile()));
                writer.write(gson.toJson(configData));

                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (configData.noiseGenerator == null) {
            System.out.println("[Simplex Terrain] The noise generator was null! Falling back to Simplex!");
            configData.noiseGenerator = NoiseType.SIMPLEX;
        }
        return configData;
    }
}
