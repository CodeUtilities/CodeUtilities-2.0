package io.github.codeutilities.mod.commands.impl.other;

import com.mojang.brigadier.CommandDispatcher;
import io.github.codeutilities.mod.commands.Command;
import io.github.codeutilities.mod.commands.arguments.ArgBuilder;
import io.github.codeutilities.mod.features.scripting.menu.ScriptsMenu;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;

public class ScriptsCommand extends Command {

    @Override
    public void register(MinecraftClient mc, CommandDispatcher<FabricClientCommandSource> cd) {
        cd.register(ArgBuilder.literal("scripts")
            .executes(ctx -> {
                ScriptsMenu menu = new ScriptsMenu();
                menu.scheduleOpenGui(menu);
                return 1;
            })
        );
    }

    @Override
    public String getDescription() {
        return """
            [blue]/scripts[reset]

            Opens a menu for creating, importing and editing scripts.""";
    }

    @Override
    public String getName() {
        return "/scripts";
    }
}
