package io.github.codeutilities.util;

import io.github.codeutilities.CodeUtilities;
import net.minecraft.client.player.LocalPlayer;

public class ChatUtil {

    public static void displayClientMessage(String msg) {
        LocalPlayer player = CodeUtilities.MC.player;

        if (player != null) player.displayClientMessage(ComponentUtil.fromString(msg), false);
    }
}
