package io.github.codeutilities.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import io.github.codeutilities.commands.sys.Command;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.commands.SharedSuggestionProvider;

public class CommandHandler {

    public static final int CANCEL_MESSAGE = new Random().nextInt();
    public static final int PASS_MESSAGE = 0;
    public static CommandDispatcher<SharedSuggestionProvider> dispatcher;
    private static final List<Command> commands = new ArrayList<>();

    public static void init(CommandDispatcher<SharedSuggestionProvider> dispatcher) {
        CommandHandler.dispatcher = dispatcher;

        register(
            new CodeUtilitiesCommand(),
            new WolframCommand(),
            new EditNbtCommand(),
            new ScriptsCommand()
        );
    }

    private static void register(Command... cmds) {
        commands.addAll(List.of(cmds));
        for (Command cmd : cmds) {
            cmd.register(dispatcher);
        }
    }

    public static LiteralArgumentBuilder<SharedSuggestionProvider> literal(String string) {
        return LiteralArgumentBuilder.literal(string);
    }

    public static <T> RequiredArgumentBuilder<SharedSuggestionProvider, T> argument(String string, ArgumentType<T> argumentType) {
        return RequiredArgumentBuilder.argument(string, argumentType);
    }

    public static List<Command> getCommands() {
        return commands;
    }
}