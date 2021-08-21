package io.github.codeutilities.sys.aweslib;

import io.github.codeutilities.sys.player.chat.ChatUtil;

public class AWEUtils {

    private static String prefix = "< aweslib > ";

    public static void sendMessage(String Message) {
        ChatUtil.sendMessage(prefix + Message);
    }

    public static String legalizeUrl(String name) {
        return name.replace("/", "").replace(":", "").replace(".", "") + ".wav";
    } // Yes it does add .wav at the end dont complain at my code D::::

}
