package me.reasonless.codeutilities.commands.nbs;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.realmsclient.gui.ChatFormatting;

import io.github.cottonmc.clientcommands.CottonClientCommandSource;
import me.reasonless.codeutilities.util.TemplateNBT;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.LiteralText;

public class NBSPlayerCommand {
	public static int execute(MinecraftClient mc, CommandContext<CottonClientCommandSource> ctx) throws Exception {
		ItemStack stack = new ItemStack(Items.JUKEBOX);

		TemplateNBT.setTemplateNBTGZIP(stack, "Music Player", "CodeUtilities", "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");
		stack.setCustomName(new LiteralText(ChatFormatting.DARK_PURPLE + "" + ChatFormatting.BOLD + "MUSIC PLAYER PACK"));

		mc.interactionManager.clickCreativeStack(stack, 36 + mc.player.inventory.selectedSlot);
		
		mc.player.sendMessage(new LiteralText(ChatFormatting.GOLD + " - " + ChatFormatting.YELLOW + "You have been given a music player pack!"));
		mc.player.sendMessage(new LiteralText(ChatFormatting.GOLD + " - " + ChatFormatting.YELLOW + "Place this in your codespace and open the chest to get music functions!"));
		mc.player.sendMessage(new LiteralText(ChatFormatting.DARK_PURPLE + " - " + ChatFormatting.LIGHT_PURPLE + "If you don't know how to use, check our discord!"));
		mc.player.sendMessage(new LiteralText(ChatFormatting.DARK_AQUA + " - " + ChatFormatting.AQUA + "" + ChatFormatting.UNDERLINE + "https://discord.gg/QBmXaGb"));

		return 1;
	}
}
