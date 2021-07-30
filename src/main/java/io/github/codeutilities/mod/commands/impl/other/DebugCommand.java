package io.github.codeutilities.mod.commands.impl.other;

import com.google.gson.JsonArray;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.mod.features.social.tab.CodeUtilitiesServer;
import io.github.codeutilities.mod.features.social.tab.User;
import io.github.codeutilities.mod.commands.Command;
import io.github.codeutilities.mod.commands.arguments.ArgBuilder;
import io.github.codeutilities.sys.player.chat.MessageGrabber;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.List;

public class DebugCommand extends Command {
    @Override
    public void register(MinecraftClient mc, CommandDispatcher<FabricClientCommandSource> cd) {
        if (CodeUtilities.BETA) {
            cd.register(ArgBuilder.literal("delayMessages")
                    .then(ArgBuilder.argument("messages", IntegerArgumentType.integer(0, 10))
                            .executes(ctx -> {
                                int messages = ctx.getArgument("messages", Integer.class);
                                mc.player.sendMessage(Text.of("[Debug] The next " + messages + " messages will be delayed."), false);
                                MessageGrabber.grabSilently(messages, msgs -> msgs.forEach(m -> mc.player.sendMessage(m, false)));
                                return 1;
                            })));
            cd.register(ArgBuilder.literal("cuplayerinfo")
                    .then(ArgBuilder.argument("query", StringArgumentType.word())
                            .executes(context -> {
                                String query = StringArgumentType.getString(context, "query");
                                if (query.equals("all")) {
                                    JsonArray users = CodeUtilitiesServer.getUsers();
                                    context.getSource().sendFeedback(Text.of("All online users: " + CodeUtilitiesServer.getUserAmount()));
                                    for (int i = 0; i < users.size(); i++) {
                                        context.getSource().sendFeedback(Text.of(users.get(i).toString()));
                                    }
                                }
                                else {
                                    User user = CodeUtilitiesServer.getUser(query);
                                    if (user == null) context.getSource().sendError(Text.of("Unable to find user."));

                                    context.getSource().sendFeedback(Text.of(user.toString()));
                                }
                                return 1;
                            })));
        }
    }
}
