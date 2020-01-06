package com.lolpop.templateutilities.clientcommands;

import com.lolpop.templateutilities.commands.Commands;

public final class ClientCommands
{
    public static ClientCommandPlugin commands = new Commands();

    public static ClientCommandPlugin getCommands()
    {
        return commands;
    }
}
