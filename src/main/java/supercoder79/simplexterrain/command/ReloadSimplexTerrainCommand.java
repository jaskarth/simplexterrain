package supercoder79.simplexterrain.command;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.registry.CommandRegistry;
import net.minecraft.server.command.CommandManager;
import supercoder79.simplexterrain.SimplexTerrain;
import supercoder79.simplexterrain.configs.ConfigData;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ReloadSimplexTerrainCommand {
    public static void register() {
        CommandRegistry.INSTANCE.register(false, dispatcher -> {
            LiteralArgumentBuilder l = LiteralArgumentBuilder.literal("reloadSimplexConfig")
                    .executes(ctx -> {
                        GsonBuilder builder = new GsonBuilder();
                        builder.setPrettyPrinting();
                        Gson gson = builder.create();
                        Path configDir = Paths.get("", "config", "simplexterrain.json");
                        if (Files.exists(configDir)) {
                            try {
                                SimplexTerrain.CONFIG = gson.fromJson(new FileReader(configDir.toFile()), ConfigData.class);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                        return 1;
                    });
            dispatcher.register(l);
        });
    }
}
