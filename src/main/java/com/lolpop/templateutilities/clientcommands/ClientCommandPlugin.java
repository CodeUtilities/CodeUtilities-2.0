package com.lolpop.templateutilities.clientcommands;

import com.mojang.brigadier.CommandDispatcher;

public interface ClientCommandPlugin
{
    void registerCommands(CommandDispatcher<LolpopClientCommandSource> dispatcher);
}
