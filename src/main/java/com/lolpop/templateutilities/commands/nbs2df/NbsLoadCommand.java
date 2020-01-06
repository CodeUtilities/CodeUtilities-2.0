package com.lolpop.templateutilities.commands.nbs2df;

import com.lolpop.templateutilities.clientcommands.LolpopClientCommandSource;
import com.lolpop.templateutilities.nbs.NBSToTemplate;
import com.lolpop.templateutilities.nbs.NBSDecoder;
import com.lolpop.templateutilities.nbs.SongData;
import com.lolpop.templateutilities.util.TemplateNBT;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.text.StringTextComponent;

import java.io.File;

public class NbsLoadCommand {
    public static int execute(Minecraft mc, CommandContext<LolpopClientCommandSource> ctx) throws Exception {
        File f = new File("CodeUtilities/NBS Files/" + StringArgumentType.getString(ctx, "location") + (StringArgumentType.getString(ctx, "location").endsWith(".nbs") ? "" : ".nbs"));

        if(f.exists()) {
            SongData d = NBSDecoder.parse(f);
            String code = new NBSToTemplate(d).convert();
            ItemStack stack = new ItemStack(Items.NOTE_BLOCK);
            TemplateNBT.setTemplateNBT(stack, d.getName(), d.getAuthor(), code);

            if(d.getName().length() == 0) {
                stack.setDisplayName(new StringTextComponent(ChatFormatting.DARK_PURPLE + "SONG " + ChatFormatting.GRAY + " - " + ChatFormatting.WHITE + " " + d.getFileName()));
            }else {
                stack.setDisplayName(new StringTextComponent(ChatFormatting.DARK_PURPLE + "SONG " + ChatFormatting.GRAY + " - " + ChatFormatting.WHITE + " " + d.getName()));
            }

            mc.playerController.sendPacketDropItem(stack);

            mc.player.sendMessage(new StringTextComponent(ChatFormatting.GOLD + " - " + ChatFormatting.YELLOW + "Loaded nbs! Check your inventory."));
            mc.player.sendMessage(new StringTextComponent(ChatFormatting.DARK_AQUA + " - " + ChatFormatting.AQUA + "If you need a nbs player do /nbs player to get one!"));
        }else {
            mc.player.sendMessage(new StringTextComponent(ChatFormatting.DARK_RED + " - " + ChatFormatting.RED + "That nbs doesn't exist."));
        }
        return 1;
    }
}
