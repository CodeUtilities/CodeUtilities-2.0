package com.lolpop.templateutilities.clientcommands.impl;

import com.lolpop.templateutilities.clientcommands.ClientCommands;
import com.lolpop.templateutilities.clientcommands.LolpopClientCommandSource;
import com.lolpop.templateutilities.commands.Commands;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import java.util.Collections;

public final class CommandCache {
    private CommandCache() {}

    private static final CommandDispatcher<LolpopClientCommandSource> DISPATCHER = new CommandDispatcher<>();

    public static void build() {
        ClientCommands.getCommands().registerCommands(DISPATCHER);
    }

    public static int execute(String input, LolpopClientCommandSource source) throws CommandSyntaxException {
        return DISPATCHER.execute(input, source);
    }

    public static boolean hasCommand(String name) {
        return DISPATCHER.findNode(Collections.singleton(name)) != null;
    }
}
