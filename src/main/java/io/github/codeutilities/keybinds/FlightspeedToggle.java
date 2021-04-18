package io.github.codeutilities.keybinds;

import io.github.codeutilities.config.CodeUtilsConfig;
import net.minecraft.client.MinecraftClient;

public class FlightspeedToggle {
    public static boolean fs_is_normal = true;

    public static void toggleFlightspeed(String mode) {
        MinecraftClient mc = MinecraftClient.getInstance();

        assert mc.player != null;
        if (fs_is_normal) {
            fs_is_normal = false;
            switch (mode) {
                case "medium":
<<<<<<< HEAD
                    mc.player.sendChatMessage("/fs " + CodeUtilsConfig.getInt("fsMed"));
                    return;
                case "fast":
                    mc.player.sendChatMessage("/fs " + CodeUtilsConfig.getInt("fsFast"));
            }
        } else mc.player.sendChatMessage("/fs " + CodeUtilsConfig.getInt("fsNormal"));
=======
                    mc.player.sendChatMessage("/fs " + CodeUtilsConfig.fsMed);
                    return;
                case "fast":
                    mc.player.sendChatMessage("/fs " + CodeUtilsConfig.fsFast);
            }
        } else mc.player.sendChatMessage("/fs " + CodeUtilsConfig.fsNormal);
>>>>>>> 0bee843 (Initial commit)
        fs_is_normal = true;
    }
}
