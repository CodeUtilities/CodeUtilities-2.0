package io.github.codeutilities.sys.aweslib;

import io.github.codeutilities.sys.player.chat.ChatUtil;

public class AweUtils {

    private static String prefix = "< aweslib > ";

    public static void sendMessage(String Message) {
        ChatUtil.sendMessage(prefix + Message);
    }

}
