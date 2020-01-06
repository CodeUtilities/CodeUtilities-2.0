package com.lolpop.templateutilities.commands;

import com.google.gson.Gson;
import com.lolpop.templateutilities.clientcommands.LolpopClientCommandSource;
import com.lolpop.templateutilities.util.TemplateJson;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.ForgeHooks;

public class WebViewCommand
{
    public static <CompoundTag> int execute(Minecraft mc, CommandContext<LolpopClientCommandSource> ctx) {
        ItemStack stack = mc.player.getHeldItem(Hand.MAIN_HAND);
        CompoundNBT tag = stack.getTag();

        try {
            CompoundNBT publicBukkitNBT = tag.getCompound("PublicBukkitValues");
            String template = publicBukkitNBT.getString("hypercube:codetemplatedata");

            TemplateJson templateJson = new Gson().fromJson(template, TemplateJson.class);

            String code = templateJson.code;
            String derpyStuff = "https://derpystuff.gitlab.io/code/?template=%s";

            mc.player.sendMessage(ForgeHooks.newChatWithLinks(String.format(derpyStuff, code)));
        }catch(NullPointerException e) {
            e.printStackTrace();
            mc.player.sendMessage(new StringTextComponent(ChatFormatting.DARK_RED + " - " + ChatFormatting.RED + "The item you are holding is not a valid template."));
            return 1;
        }
        return 1;
    }
}
