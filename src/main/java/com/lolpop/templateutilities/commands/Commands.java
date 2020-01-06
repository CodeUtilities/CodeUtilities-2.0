package com.lolpop.templateutilities.commands;

import com.lolpop.templateutilities.clientcommands.ArgumentBuilders;
import com.lolpop.templateutilities.clientcommands.ClientCommandPlugin;
import com.lolpop.templateutilities.clientcommands.LolpopClientCommandSource;
import com.lolpop.templateutilities.commands.env2df.EnvCommand;
import com.lolpop.templateutilities.commands.env2df.EnvLoadCommand;
import com.lolpop.templateutilities.commands.env2df.EnvSenderCommand;
import com.lolpop.templateutilities.commands.nbs2df.NbsLoadCommand;
import com.lolpop.templateutilities.commands.nbs2df.NBSCommand;
import com.lolpop.templateutilities.commands.nbs2df.NbsPlayerCommand;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.StringTextComponent;

public class Commands implements ClientCommandPlugin
{
    Minecraft mc = Minecraft.getInstance();
    private static final CommandDispatcher<LolpopClientCommandSource> DISPATCHER = new CommandDispatcher<>();

    @Override
    public void registerCommands(CommandDispatcher<LolpopClientCommandSource> dispatcher)
    {
        dispatcher.register(ArgumentBuilders.literal("nbs")
                .then(ArgumentBuilders.literal("player")
                        .executes(ctx -> {
                            try {
                                return NbsPlayerCommand.execute(mc, ctx);
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                            return 0;
                        }))
                .then(ArgumentBuilders.literal("load")
                        .then(ArgumentBuilders.argument("location", StringArgumentType.greedyString())
                                .executes(ctx -> {
                                    try {
                                        return NbsLoadCommand.execute(mc, ctx);
                                    } catch (Exception e) {
                                        mc.player.sendMessage(new StringTextComponent(ChatFormatting.DARK_RED + " - " + ChatFormatting.RED + "There was an error loading this song."));
                                        mc.player.sendMessage(new StringTextComponent(ChatFormatting.DARK_RED + " - " + ChatFormatting.RED + "Maybe the song is made using an older Noteblock Studio Version."));
                                        mc.player.sendMessage(new StringTextComponent(ChatFormatting.GOLD + " - " + ChatFormatting.YELLOW + "NBS2DF Supports 3.6.0"));
                                        e.printStackTrace();
                                    }
                                    return 1;
                                })))
                .executes(ctx -> NBSCommand.execute(mc, ctx)));

        dispatcher.register(ArgumentBuilders.literal("env")
                .then(ArgumentBuilders.literal("sender")
                        .executes(ctx -> EnvSenderCommand.execute(mc, ctx))
                )
                .then(ArgumentBuilders.literal("load")
                        .then(ArgumentBuilders.argument("location", StringArgumentType.greedyString())
                                /*.then(ArgumentBuilders.argument("xoff", IntegerArgumentType.integer())
                                        .then(ArgumentBuilders.argument("yoff", IntegerArgumentType.integer())
                                                .then(ArgumentBuilders.argument("zoff", IntegerArgumentType.integer())
                                                        .executes(ctx -> {
                                                            try {
                                                                return EnvLoadCommand.execute(mc, ctx, true);
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }
                                                            return 1;
                                                        }))))*/
                                .executes(ctx -> {
                                    try {
                                        return EnvLoadCommand.execute(mc, ctx, false);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    return 1;
                                })))
                .executes(ctx -> EnvCommand.execute(mc, ctx))
        );

        dispatcher.register(ArgumentBuilders.literal("webview")
                .executes(ctx -> WebViewCommand.execute(mc, ctx)));
    }
}
