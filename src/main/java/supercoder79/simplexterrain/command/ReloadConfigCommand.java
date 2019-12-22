package supercoder79.simplexterrain.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.fabricmc.fabric.api.registry.CommandRegistry;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import supercoder79.simplexterrain.configs.Config;

public class ReloadConfigCommand {
	public static void init() {
		CommandRegistry.INSTANCE.register(false, dispatcher -> {
			LiteralArgumentBuilder<ServerCommandSource> lab = CommandManager.literal("reloadterrainconfig").requires(executor -> executor.hasPermissionLevel(2)).executes(cmd -> {
				ServerCommandSource source = cmd.getSource();
				Config.init();
				source.sendFeedback(new LiteralText("§2§lReloaded Configs!"), true);
				return 1;
			});
			dispatcher.register(lab);
		});
	}
}
