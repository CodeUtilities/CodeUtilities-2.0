package io.github.codeutilities.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.BuiltInExceptionProvider;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.commands.impl.CodeUtilitiesCommand;
import io.github.codeutilities.commands.impl.EditNbtCommand;
import io.github.codeutilities.commands.impl.ScriptsCommand;
import io.github.codeutilities.commands.impl.WolframCommand;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.TextComponent;

import java.util.ArrayList;
import java.util.List;

public class CommandHandler {

    private static final List<Command> commands = new ArrayList<>();
    public static CommandDispatcher<SharedSuggestionProvider> dispatcher;

    public static void init() {
        CommandHandler.dispatcher = new CommandDispatcher<>();

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

    public static boolean dispatch(StringReader reader) {
        if (CodeUtilities.MC.getConnection() != null) {
            // get the command dispatcher
            try {
                // execute the command
                dispatcher.execute(reader, null);
                return true;
            } catch (CommandSyntaxException e) {
                // if the command doesn't exist an exception will be thrown
                BuiltInExceptionProvider builtInExceptions = CommandSyntaxException.BUILT_IN_EXCEPTIONS;

                if (e.getType() != builtInExceptions.dispatcherUnknownCommand() &&
                        e.getType() != builtInExceptions.dispatcherParseException()) {
                    CodeUtilities.MC.player.sendMessage(new TextComponent(e.getMessage())
                                    .withStyle(style -> style.withColor(ChatFormatting.RED)), null);
                    return true;
                }
            }
        }
        return false;
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