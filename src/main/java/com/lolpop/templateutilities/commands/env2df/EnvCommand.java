package com.lolpop.templateutilities.commands.env2df;

import com.lolpop.templateutilities.clientcommands.LolpopClientCommandSource;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.StringTextComponent;

public class EnvCommand
{
    public static int execute(Minecraft mc, CommandContext<LolpopClientCommandSource> ctx)
    {
        mc.player.sendMessage(new StringTextComponent(ChatFormatting.GRAY + "" + ChatFormatting.STRIKETHROUGH + "========================================="));
        mc.player.sendMessage(new StringTextComponent(ChatFormatting.YELLOW + "/env load <file>"));
        mc.player.sendMessage(new StringTextComponent("Generates a code template for the file."));
        mc.player.sendMessage(new StringTextComponent(" "));
        mc.player.sendMessage(new StringTextComponent(ChatFormatting.YELLOW + "/env sender"));
        mc.player.sendMessage(new StringTextComponent("Gives you the environment loader that can load environments."));
        mc.player.sendMessage(new StringTextComponent(ChatFormatting.GRAY + "" + ChatFormatting.STRIKETHROUGH + "========================================="));
        return 1;
    }
}
