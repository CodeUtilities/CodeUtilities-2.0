package io.github.codeutilities.sys.renderer;

import java.awt.Color;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;

public class RenderUtil extends DrawableHelper {
    public static void drawGradientRect(MatrixStack matrices, int xStart, int yStart, int xEnd, int yEnd, Color colorStart, Color colorEnd, int zOffset) {
        DrawableHelper.fillGradient(matrices, xStart, yStart, xEnd, yEnd, colorStart.getRGB(),colorEnd.getRGB(), zOffset);
    }
}