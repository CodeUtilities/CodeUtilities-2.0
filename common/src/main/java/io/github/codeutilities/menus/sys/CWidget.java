package io.github.codeutilities.menus.sys;

import com.mojang.blaze3d.vertex.PoseStack;

public interface CWidget {

    void render(PoseStack stack, int mouseX, int mouseY, float tickDelta);

    default void mouseClicked(double x, double y, int button) {}

    default void charTyped(char ch, int keyCode) {}

    default void keyPressed(int keyCode, int scanCode, int modifiers) {}

    default void mouseScrolled(double mouseX, double mouseY, double amount) {}
}
