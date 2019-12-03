package supercoder79.simplexterrain.configs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Config {
    public static ConfigData init() {
        ConfigData configHolder = null;
        try {
            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting();
            Gson gson = builder.create();
            Path configDir = Paths.get("", "config", "simplexterrain.cfg");
            if (Files.exists(configDir)) {
                configHolder = gson.fromJson(new FileReader(configDir.toFile()), ConfigData.class);
            } else {
                configHolder = new ConfigData();
                BufferedWriter writer = new BufferedWriter(new FileWriter(configDir.toFile()));
                writer.write(gson.toJson(configHolder));

                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return configHolder;
    }
}
