package supercoder79.simplexterrain.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.command.suggestion.SuggestionProviders;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import supercoder79.simplexterrain.world.noisetype.NoiseType;
import supercoder79.simplexterrain.world.noisetype.NoiseTypeCache;
import supercoder79.simplexterrain.world.noisetype.NoiseTypeHolder;
import supercoder79.simplexterrain.world.noisetype.NoiseTypePicker;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapNoiseTypeCommand {
    public static void init() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            LiteralArgumentBuilder<ServerCommandSource> builder = CommandManager.literal("mapnoisetype")
                    .requires(source -> source.hasPermissionLevel(2));

            builder.then(CommandManager.argument("biome", IdentifierArgumentType.identifier()).suggests(SuggestionProviders.ALL_BIOMES).executes((commandContext) -> {
                return execute(commandContext.getSource(), commandContext.getArgument("biome", Identifier.class));
            }));

            dispatcher.register(builder);

        });
    }

    private static int execute(ServerCommandSource source, Identifier biome) {
        Registry<Biome> biomes = source.getWorld().getRegistryManager().get(Registry.BIOME_KEY);

        RegistryKey<Biome> key = biomes.getKey(biomes.get(biome)).get();

        NoiseTypeCache cache = NoiseTypeHolder.get(key);
        if (cache != null) {
            NoiseTypePicker picker = cache.getPicker();
            List<NoiseType> types = new ArrayList<>(picker.getPoints().values());
            Map<NoiseType, Integer> counts = new HashMap<>();

            int increment = 255 / types.size();
            int total = 0;

            BufferedImage img = new BufferedImage(2048, 2048, BufferedImage.TYPE_INT_RGB);

            for (int x = -1024; x < 1024; x++) {
                if (x % 256 == 0) {
                    source.sendFeedback(new LiteralText(((x + 1024) / 2048.0) * 100 + "%"), false);
                }

                for (int z = -1024; z < 1024; z++) {
                    NoiseType type = picker.get(x, z);
                    int color = types.indexOf(type) * increment;
                    counts.put(type, counts.getOrDefault(type, 0) + 1);
                    total++;

                    img.setRGB(x + 1024, z + 1024, getIntFromColor(color, color, color));
                }
            }

            // save the noise type map
            Path p = Paths.get("types_" + biome.toString().replace(":", "_") + ".png");

            for (NoiseType type : types) {
                System.out.println(type.getClass().getSimpleName() + " -> " + counts.get(type) + " (" + ((counts.get(type) / (double)total) * 100) + ")%");
            }

            try {
                ImageIO.write(img, "png", p.toAbsolutePath().toFile());
            } catch (IOException e) {
                e.printStackTrace();
            }

            source.sendFeedback(new LiteralText("Mapped Noise types for " + biome + "!"), false);

        } else {
            source.sendError(new LiteralText("This biome does not have a noise type"));
        }

        return 1;
    }

    private static int getIntFromColor(int red, int green, int blue) {
        red = (red << 16) & 0x00FF0000; //Shift red 16-bits and mask out other stuff
        green = (green << 8) & 0x0000FF00; //Shift green 8-bits and mask out other stuff
        blue = blue & 0x000000FF; //Mask out anything not blue.

        return 0xFF000000 | red | green | blue; //0xFF000000 for 100% Alpha. Bitwise OR everything together.
    }
}
