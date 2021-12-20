package io.github.codeutilities.util;

import io.github.codeutilities.CodeUtilities;
import net.minecraft.ChatFormatting;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;

public class ChatUtil {

    public static void displayClientMessage(String msg, Style style) {
        displayClientMessage(new TextComponent(msg).setStyle(style));
    }

    public static void displayClientMessage(Component msg) {
        LocalPlayer player = CodeUtilities.MC.player;
        if (player != null) player.displayClientMessage(msg, false);
    }

    public static void displaySuccess(String msg) {
        displayClientMessage(msg, Style.EMPTY.withColor(ChatFormatting.GREEN));
    }

    public static void displayError(String msg) {
        displayClientMessage(msg, Style.EMPTY.withColor(ChatFormatting.RED));
    }
}
