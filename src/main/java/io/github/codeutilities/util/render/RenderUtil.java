package io.github.codeutilities.util.render;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;

public class RenderUtil extends DrawableHelper {
    public static void drawRect(MatrixStack matrices, int left, int top, int right, int bottom, Color color)
    {
        DrawableHelper.fill(matrices, left, top, right, bottom, color.getRGB());
    }

    public static void drawGradientRect(MatrixStack matrices, int xStart, int yStart, int xEnd, int yEnd, Color colorStart, Color colorEnd, int zOffset) {
        DrawableHelper.fillGradient(matrices, xStart, yStart, xEnd, yEnd, colorStart.getRGB(), colorEnd.getRGB(), zOffset);
    }

    protected void drawGradientRect(Matrix4f matrix, BufferBuilder bufferBuilder, int xStart, int yStart, int xEnd, int yEnd, int z, int colorStart, int colorEnd) {
        DrawableHelper.fillGradient(matrix, bufferBuilder, xStart, yStart, xEnd, yEnd, z, colorStart, colorEnd);
    }
}