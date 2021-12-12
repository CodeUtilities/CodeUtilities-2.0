package io.github.codeutilities.menu.widget;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.codeutilities.util.RenderUtil;

public class CImage implements CWidget {

    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final String img;

    public CImage(int x, int y, int width, int height, String img) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.img = img;
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float tickDelta) {
        RenderUtil.renderImage(stack, x, y, width, height, 0, 0, 1, 1, img);
    }
}
