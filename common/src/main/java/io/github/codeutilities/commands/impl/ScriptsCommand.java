package io.github.codeutilities.commands.impl;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.commands.Command;
import io.github.codeutilities.commands.CommandHandler;
import io.github.codeutilities.commands.arg.FileArgumentType;
import io.github.codeutilities.menu.ScriptEditorMenu;
import io.github.codeutilities.scripts.Script;
import io.github.codeutilities.scripts.ScriptHandler;
import io.github.codeutilities.scripts.types.ScriptValue;
import io.github.codeutilities.util.ChatUtil;
import io.github.codeutilities.util.FileUtil;
import java.io.File;
import java.util.List;
import java.util.Map.Entry;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.TextComponent;

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
                ChatUtil.displaySuccess("Scripts: " + scripts.size());
                for (Script script : scripts) {
                    ChatUtil.displaySuccess("- " + script.getName());
                }
                return 1;
            })
            .then(CommandHandler.literal("create")
                .then(CommandHandler.argument("name", StringArgumentType.greedyString())
                    .executes(ctx -> {
                            String name = StringArgumentType.getString(ctx, "name");

                            if (ScriptHandler.createScript(name)) {
                                ChatUtil.displaySuccess("Created script " + name);
                                ScriptHandler.loadScripts();
                                ChatUtil.displaySuccess("Reloaded scripts");
                            } else {
                                ChatUtil.displayError("Failed to create script " + name);
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
                                ChatUtil.displayError("Script " + name + " does not exist.");
                            }

                            return 1;
                        }
                    )
                )
            )
            .then(CommandHandler.literal("reload")
                .executes(ctx -> {
                        ScriptHandler.loadScripts();
                        ChatUtil.displaySuccess("Reloaded scripts");
                        return 1;
                    }
                )
            )
            .then(CommandHandler.literal("delete")
                .then(CommandHandler.argument("name", new FileArgumentType(scriptFolder))
                    .executes(ctx -> {
                        String name = StringArgumentType.getString(ctx, "name");

                        if (ScriptHandler.deleteScript(name)) {
                            ChatUtil.displaySuccess("Deleted script " + name);
                            ScriptHandler.loadScripts();
                            ChatUtil.displaySuccess("Reloaded scripts");
                        } else {
                            ChatUtil.displayError("Failed to delete script " + name);
                        }

                        return 1;
                    })
                )
            ).then(CommandHandler.literal("vars")
                .then(CommandHandler.argument("script", new FileArgumentType(scriptFolder))
                    .executes(ctx -> {
                        String name = StringArgumentType.getString(ctx, "script");

                        for (Script s : ScriptHandler.scripts) {
                            if (s.getName().equals(name)) {
                                for (Entry<String, ScriptValue> entry : s.getContext().getVars().entrySet()) {
                                    ChatUtil.displayClientMessage(new TextComponent(entry.getKey()+": ")
                                        .withStyle(ChatFormatting.GREEN)
                                        .append(new TextComponent(entry.getValue().text())
                                            .withStyle(ChatFormatting.AQUA)
                                        )
                                    );
                                }
                            }
                        }

                        return 1;
                    })
                )
            )

        );
    }
}
