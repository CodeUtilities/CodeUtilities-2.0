package io.github.codeutilities.menus.sys;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.util.RenderUtil;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;

public class CPanel {

    final List<CWidget> children = new ArrayList<>();
    final int width, height;

    public CPanel(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void render(PoseStack stack, int mouseX, int mouseY, float tickDelta) {
        stack.pushPose();
        Minecraft mc = CodeUtilities.MC;

        stack.translate(mc.screen.width/2, mc.screen.height/2, 0);

        float s = (float) mc.getWindow().getGuiScale();
        stack.scale(s,s,0);

        stack.translate(-width/2, -height/2, 0);

        RenderUtil.renderGui(stack,0,0,width,height);

        mouseX += -mc.screen.width/2;
        mouseY += -mc.screen.height/2;

        mouseX /= s;
        mouseY /= s;

        mouseX += width/2;
        mouseY += height/2;

        for (CWidget cWidget : children) {
            cWidget.render(stack, mouseX, mouseY, tickDelta);
        }
        stack.popPose();
    }

    public void mouseClicked(double mouseX, double mouseY, int button) {
        mouseX = translateMouseX(mouseX);
        mouseY = translateMouseY(mouseY);

        for (CWidget cWidget : children) {
            cWidget.mouseClicked(mouseX, mouseY, button);
        }
    }

    private double translateMouseX(double mouseX) {
        Minecraft mc = CodeUtilities.MC;
        float s = (float) mc.getWindow().getGuiScale();
        mouseX += -mc.screen.width/2;
        mouseX /= s;
        mouseX += width/2;
        return mouseX;
    }

    private double translateMouseY(double mouseY) {
        Minecraft mc = CodeUtilities.MC;
        float s = (float) mc.getWindow().getGuiScale();
        mouseY += -mc.screen.height/2;
        mouseY /= s;
        mouseY += height/2;
        return mouseY;
    }


    public void add(CWidget cImage) {
        children.add(cImage);
    }

    public void charTyped(char ch, int keyCode) {
        for (CWidget cWidget : children) {
            cWidget.charTyped(ch, keyCode);
        }
    }

    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        for (CWidget cWidget : children) {
            cWidget.keyPressed(keyCode, scanCode, modifiers);
        }
    }

    public void mouseScrolled(double mouseX, double mouseY, double amount) {
        mouseX = translateMouseX(mouseX);
        mouseY = translateMouseY(mouseY);

        for (CWidget cWidget : children) {
            cWidget.mouseScrolled(mouseX, mouseY, amount);
        }
    }
}
