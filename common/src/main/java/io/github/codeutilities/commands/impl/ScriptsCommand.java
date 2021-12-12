package io.github.codeutilities.commands.impl;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.commands.Command;
import io.github.codeutilities.commands.CommandHandler;
import io.github.codeutilities.commands.FileArgumentType;
import io.github.codeutilities.menus.ScriptEditorMenu;
import io.github.codeutilities.scripts.Script;
import io.github.codeutilities.scripts.ScriptHandler;
import io.github.codeutilities.util.ChatUtil;
import io.github.codeutilities.util.FileUtil;
import java.io.File;
import java.util.List;
import net.minecraft.commands.SharedSuggestionProvider;

public class ScriptsCommand implements Command {

    @Override
    public String getName() {
        return "/scripts";
    }

    @Override
    public String getDescription() {
        return "Command for making and loading scripts.";
    }

    @Override
    public void register(CommandDispatcher<SharedSuggestionProvider> dispatcher) {
        File scriptFolder = FileUtil.cuFolder("Scripts").toFile();

        dispatcher.register(CommandHandler.literal("scripts")
            .executes(ctx -> {
                List<Script> scripts = ScriptHandler.scripts;
                ChatUtil.displayClientMessage("§aScripts: " + scripts.size());
                for (Script script : scripts) {
                    ChatUtil.displayClientMessage("§a- " + script.getName());
                }
                return 1;
            })
            .then(CommandHandler.literal("create")
                .then(CommandHandler.argument("name", StringArgumentType.greedyString())
                    .executes(ctx -> {
                            String name = StringArgumentType.getString(ctx, "name");

                            if (ScriptHandler.createScript(name)) {
                                ChatUtil.displayClientMessage("§aCreated script " + name);
                                ScriptHandler.load();
                                ChatUtil.displayClientMessage("§aReloaded scripts");
                            } else {
                                ChatUtil.displayClientMessage("§cFailed to create script " + name);
                            }

                            return 1;
                        }
                    )
                )
            )
            .then(CommandHandler.literal("edit")
                .then(CommandHandler.argument("name", new FileArgumentType(scriptFolder))
                    .executes(ctx -> {
                            String name = StringArgumentType.getString(ctx, "name");

                            final String finalName = name;
                            if (ScriptHandler.scripts.stream().anyMatch(script -> script.getName().equalsIgnoreCase(finalName))) {
                                CodeUtilities.MC.tell(() -> CodeUtilities.MC.setScreen(new ScriptEditorMenu(finalName)));
                            } else {
                                ChatUtil.displayClientMessage("§cScript " + name + " does not exist.");
                            }

                            return 1;
                        }
                    )
                )
            )
            .then(CommandHandler.literal("reload")
                .executes(ctx -> {
                        ScriptHandler.load();
                        ChatUtil.displayClientMessage("§aReloaded scripts");
                        return 1;
                    }
                )
            )
            .then(CommandHandler.literal("delete")
                .then(CommandHandler.argument("name", new FileArgumentType(scriptFolder))
                    .executes(ctx -> {
                        String name = StringArgumentType.getString(ctx, "name");

                        if (ScriptHandler.deleteScript(name)) {
                            ChatUtil.displayClientMessage("§aDeleted script " + name);
                            ScriptHandler.load();
                            ChatUtil.displayClientMessage("§aReloaded scripts");
                        } else {
                            ChatUtil.displayClientMessage("§cFailed to delete script " + name);
                        }

                        return 1;
                    })
                )
            )

        );
    }
}
