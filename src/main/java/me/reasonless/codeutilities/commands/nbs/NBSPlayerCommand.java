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
