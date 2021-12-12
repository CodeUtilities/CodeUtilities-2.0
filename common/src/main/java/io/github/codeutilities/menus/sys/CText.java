package io.github.codeutilities.menus.sys;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.codeutilities.CodeUtilities;
import net.minecraft.client.gui.Font;

public class CText implements CWidget {

    int x;
    int y;
    String text;
    int color;
    float scale;
    boolean centered;
    boolean shadow;

    public CText(int x, int y, String text, int color, float scale, boolean centered, boolean shadow) {
        this.x = x;
        this.y = y;
        this.text = text;
        this.color = color;
        this.scale = scale;
        this.centered = centered;
        this.shadow = shadow;
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float tickDelta) {
        stack.pushPose();
        stack.translate(x, y, 0);

        stack.scale(0.5f, 0.5f, 0.5f);

        Font f = CodeUtilities.MC.font;

        if (centered) {
            stack.translate(-f.width(text) / 2, -f.lineHeight / 2, 0);
        }

        if (shadow) {
            f.drawShadow(stack, text, 0, 0, color);
        } else {
            f.draw(stack, text, 0, 0, color);
        }
        stack.popPose();
    }

    public void setText(String t) {
        text = t;
    }
}
