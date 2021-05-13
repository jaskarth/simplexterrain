package supercoder79.simplexterrain.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import supercoder79.simplexterrain.api.Heightmap;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MapHeightmapCommand {
    public static void init() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            LiteralArgumentBuilder<ServerCommandSource> builder = CommandManager.literal("mapheightmap")
                    .requires(source -> source.hasPermissionLevel(2));

            builder.executes(context -> execute(context.getSource()));

            dispatcher.register(builder);

        });
    }

    private static int execute(ServerCommandSource source) {
        BufferedImage img = new BufferedImage(2048, 2048, BufferedImage.TYPE_INT_RGB);

        // Unsafe cast but we can hopefully expect people to be safe in dev :P
        Heightmap heightmap = (Heightmap) source.getWorld().getChunkManager().getChunkGenerator();

        for (int x = -1024; x < 1024; x++) {
            if (x % 256 == 0) {
                source.sendFeedback(new LiteralText(((x + 1024) / 2048.0) * 100 + "%"), false);
            }

            for (int z = -1024; z < 1024; z++) {
                int height = heightmap.getHeight(x, z);

                img.setRGB(x + 1024, z + 1024, getIntFromColor(height, height, height));
            }
        }

        // save the biome map
        Path p = Paths.get("heightmap.png");
        try {
            ImageIO.write(img, "png", p.toAbsolutePath().toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }

        source.sendFeedback(new LiteralText("Mapped heightmap!"), false);

        return 0;
    }

    private static int getIntFromColor(int red, int green, int blue) {
        red = (red << 16) & 0x00FF0000; //Shift red 16-bits and mask out other stuff
        green = (green << 8) & 0x0000FF00; //Shift green 8-bits and mask out other stuff
        blue = blue & 0x000000FF; //Mask out anything not blue.

        return 0xFF000000 | red | green | blue; //0xFF000000 for 100% Alpha. Bitwise OR everything together.
    }
}
