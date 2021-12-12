package io.github.codeutilities.menus.sys;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.codeutilities.util.RenderUtil;

public record CImage(int x, int y, int width, int height, String img) implements CWidget {

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float tickDelta) {
        RenderUtil.renderImage(stack, x, y, width, height, 0, 0, 1, 1, img);
    }
}
