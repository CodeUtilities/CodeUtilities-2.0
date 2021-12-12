package io.github.codeutilities.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.TextComponent;

public class TextUtil {

    public static TextComponent txt2Comp(String message) {
        TextComponent result = new TextComponent("");

        try {
            Pattern pattern = Pattern.compile("(§[a-f0-9lonmkrA-FLONMRK]|§x(§[a-f0-9A-F]){6})");
            Matcher matcher = pattern.matcher(message);

            Style s = Style.EMPTY;

            int lastIndex = 0;
            while (matcher.find()) {
                int start = matcher.start();
                String text = message.substring(lastIndex, start);
                if (text.length() != 0) {
                    TextComponent t = new TextComponent(text);
                    t.setStyle(s);
                    result.append(t);
                }
                String col = matcher.group();

                if (col.length() == 2) {
                    s = s.applyFormat(ChatFormatting.getByCode(col.charAt(1)));
                } else {
                    s = Style.EMPTY.withColor(
                        TextColor.parseColor("#" + col.replaceAll("§", "").substring(1)));
                }
                lastIndex = matcher.end();
            }
            String text = message.substring(lastIndex);
            if (text.length() != 0) {
                TextComponent t = new TextComponent(text);
                t.setStyle(s);
                result.append(t);
            }
        } catch (Exception err) {
            err.printStackTrace();
            return new TextComponent("CodeUtilities Text Error");
        }

        return result;
    }
}
