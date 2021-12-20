package io.github.codeutilities.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.TextComponent;

public class ComponentUtil {

    public static TextComponent fromString(String message) {
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

    public static String toFormattedString(Component message) {
        StringBuilder result = new StringBuilder();

        Style style = message.getStyle();

        String format = "";

        if (style.getColor() != null) {
            format += "§x§" + String.join("§", String.format("%06X", style.getColor().getValue()).split(""));
        }

        if (style.isBold()) {
            format += "§l";
        }
        if (style.isItalic()) {
            format += "§o";
        }
        if (style.isUnderlined()) {
            format += "§n";
        }
        if (style.isStrikethrough()) {
            format += "§m";
        }
        if (style.isObfuscated()) {
            format += "§k";
        }

        result.append(format);
        result.append(message.getContents());

        for (Component sibling : message.getSiblings()) {
            result.append(toFormattedString(sibling));
        }

        return result.toString();
    }
}
