package io.github.codeutilities.menu.widget;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.util.RenderUtil;
import java.awt.Rectangle;
import net.minecraft.client.gui.Font;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;

public class CButton implements CWidget {

    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final String text;
    private final Runnable onClick;

    public CButton(int x, int y, int width, int height, String text, Runnable onClick) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
        this.onClick = onClick;
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float tickDelta) {
        stack.pushPose();
        stack.translate(x, y, 0);

        stack.pushPose();
        stack.scale(0.5f, 0.5f, 0.5f);

        Rectangle rect = new Rectangle(x, y, width, height);

        RenderUtil.renderButton(stack, 0, 0, width * 2, height * 2, rect.contains(mouseX, mouseY), false);
        stack.popPose();

        Font f = CodeUtilities.MC.font;

        stack.translate(rect.width / 2, rect.height / 2, 0);
        stack.scale(0.5f, 0.5f, 0.5f);
        stack.translate(-f.width(text) / 2, -f.lineHeight / 2, 0);

        f.drawShadow(stack, text, 0, 0, 0xFFFFFF);

        stack.popPose();
    }

    @Override
    public void mouseClicked(double x, double y, int button) {
        Rectangle rect = new Rectangle(this.x, this.y, width, height);

        if (rect.contains(x, y)) {
            CodeUtilities.MC.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1f));
            onClick.run();
        }

        CWidget.super.mouseClicked(x, y, button);
    }
}
