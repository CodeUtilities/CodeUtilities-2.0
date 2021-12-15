package io.github.codeutilities.menu.widget;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.util.RenderUtil;
import java.nio.FloatBuffer;
import net.minecraft.client.Minecraft;

public class CScrollPanel extends CPanel {

    private int scroll = 0;

    public CScrollPanel(int width, int height) {
        super(width, height);
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float tickDelta) {
        stack.pushPose();
        Minecraft mc = CodeUtilities.MC;

        stack.translate(mc.screen.width / 2, mc.screen.height / 2, 0);

        float s = (float) mc.getWindow().getGuiScale();
        stack.scale(s, s, 0);

        stack.translate(-width / 2, -height / 2, 0);

        RenderUtil.renderGui(stack, 0, 0, width, height);

        mouseX = (int) translateMouseX(mouseX);
        mouseY = (int) translateMouseY(mouseY);

        FloatBuffer buff = FloatBuffer.allocate(16);
        stack.last().pose().store(buff);
        RenderUtil.setScissor(
            (int) ((buff.get(12)+1) * buff.get(0)),
            (int) ((buff.get(13)+1) * buff.get(5)),
            (int) ((width-1) * buff.get(0) * 2),
            (int) ((height-1) * buff.get(5) * 2)
        );

        stack.translate(0, scroll, 0);

        for (CWidget cWidget : children) {
            cWidget.render(stack, mouseX, mouseY, tickDelta);
        }

        RenderUtil.clearScissor();
        stack.popPose();
    }

    @Override
    public void mouseScrolled(double mouseX, double mouseY, double amount) {
        scroll += amount * 5;
        super.mouseScrolled(mouseX, mouseY, amount);
    }

    @Override
    public double translateMouseY(double mouseY) {
        return super.translateMouseY(mouseY)-scroll;
    }
}
