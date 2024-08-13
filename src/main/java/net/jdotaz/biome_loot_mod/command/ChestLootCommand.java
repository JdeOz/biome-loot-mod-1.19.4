package net.jdotaz.biome_loot_mod.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.jdotaz.biome_loot_mod.util.ConfigLoader;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class ChestLootCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {
        if (dedicated) {
            dispatcher.register(CommandManager.literal("chestloot")
                    .requires(source -> source.hasPermissionLevel(2))
                    .then(CommandManager.literal("reload")
                            .executes(ChestLootCommand::run)));
        }
    }

    public static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        try {
            ConfigLoader.loadConfig();
            context.getSource().sendFeedback(Text.literal("Chest loot configuration reloaded successfully!"), true);
        } catch (Exception e) {
            context.getSource().sendError(Text.literal("Failed to reload chest loot configuration: " + e.getMessage()));
        }
        return 1;
    }
}