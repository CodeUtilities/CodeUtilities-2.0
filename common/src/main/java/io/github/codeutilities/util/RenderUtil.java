package io.github.codeutilities.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import io.github.codeutilities.CodeUtilities;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderUtil {

    public static void renderImage(PoseStack stack, int x, int y, int width, int height, float ux, float uy, float uw, float uh, String image) {
        RenderSystem.enableBlend();
        RenderSystem.setShaderTexture(0, new ResourceLocation(image));
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.disableCull();

        Tesselator tessellator = Tesselator.getInstance();

        BufferBuilder bb = tessellator.getBuilder();
        bb.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bb.vertex(stack.last().pose(), x, y, 0).uv(ux, uy).endVertex();
        bb.vertex(stack.last().pose(), x + width, y, 0).uv(ux+uw, uy).endVertex();
        bb.vertex(stack.last().pose(), x + width, y + height, 0).uv(ux+uw, uy+uh).endVertex();
        bb.vertex(stack.last().pose(), x, y + height, 0).uv(ux, uy+uh).endVertex();
        tessellator.end();
    }

    public static void renderButton(PoseStack stack, int x, int y, int width, int height, boolean hovered, boolean disabled) {
        final String image = "textures/gui/widgets.png";
        final int textureWidth = 256;
        final int textureHeight = 256;
        final int padding = 3;
        int x1 = 0;
        int y1 = 66;
        if (disabled) {
            y1 = 46;
        } else if (hovered) {
            y1 = 86;
        }
        int x2 = 200;
        int y2 = y1 + 20;
        renderContinuousTexture(stack, x, y, width, height, image, x1, y1, x2, y2, textureWidth, textureHeight, padding,1);
    }

    public static void renderContinuousTexture(PoseStack stack, int x, int y, int width, int height, String image, int tx1, int ty1, int tx2, int ty2, int textureWidth, int textureHeight, int padding,double scale) {
        int scaledPadding = (int) (padding*scale);
        //top left corner
        renderContinuousTexture(stack, x, y, scaledPadding, scaledPadding, image, tx1, ty1, tx1 + padding, ty1 + padding, textureWidth, textureHeight,scale);
        //top right corner
        renderContinuousTexture(stack, x + width - scaledPadding, y, scaledPadding, scaledPadding, image, tx2 - padding, ty1, tx2, ty1 + padding, textureWidth, textureHeight,scale);
        //bottom left corner
        renderContinuousTexture(stack, x, y + height - scaledPadding, scaledPadding, scaledPadding, image, tx1, ty2 - padding, tx1 + padding, ty2, textureWidth, textureHeight,scale);
        //bottom right corner
        renderContinuousTexture(stack, x + width - scaledPadding, y + height - scaledPadding, scaledPadding, scaledPadding, image, tx2 - padding, ty2 - padding, tx2, ty2, textureWidth, textureHeight,scale);

        //top
        renderContinuousTexture(stack, x + scaledPadding, y, width - scaledPadding * 2, scaledPadding, image, tx1 + padding, ty1, tx2 - padding, ty1 + padding, textureWidth, textureHeight,scale);
        //bottom
        renderContinuousTexture(stack, x + scaledPadding, y + height - scaledPadding, width - scaledPadding * 2, scaledPadding, image, tx1 + padding, ty2 - padding, tx2 - padding, ty2, textureWidth, textureHeight,scale);
        //left
        renderContinuousTexture(stack, x, y + scaledPadding, scaledPadding, height - scaledPadding * 2, image, tx1, ty1 + scaledPadding, tx1 + padding, ty2 - padding, textureWidth, textureHeight,scale);
        //right
        renderContinuousTexture(stack, x + width - scaledPadding, y + scaledPadding, scaledPadding, height - scaledPadding * 2, image, tx2 - padding, ty1 + padding, tx2, ty2 - padding, textureWidth, textureHeight,scale);

        //center
        renderContinuousTexture(stack, x + scaledPadding, y + scaledPadding, width - scaledPadding * 2, height - scaledPadding * 2, image, tx1 + padding, ty1 + padding, tx2 - padding, ty2 - padding, textureWidth, textureHeight,scale);
    }

    public static void renderContinuousTexture(PoseStack stack, int x, int y, int width, int height, String image, int tx1, int ty1, int tx2, int ty2, int textureWidth, int textureHeight, double scale) {
        int tw = (tx2 - tx1);
        int th = (ty2 - ty1);

        float ux = (float) tx1 / textureWidth;
        float uy = (float) ty1 / textureHeight;
        float uw = (float) tw / textureWidth;
        float uh = (float) th / textureHeight;

        tw*=scale;
        th*=scale;

        float xrepeations = (float) width / (tw);
        float yrepeations = (float) height / (th);
        for (int xi = 0; xi <= xrepeations - 1; xi++) {
            for (int yi = 0; yi <= yrepeations - 1; yi++) {
                renderImage(stack,x + xi * tw, y + yi * th, tw, th, ux, uy, uw, uh, image);
            }
        }
        float minH = Math.min(th, yrepeations * th + 1);

        for (int xi = 0; xi < xrepeations - 1; xi++) {
            renderImage(stack, x + xi * tw, Math.max((int) (y + yrepeations * th) - th, y), tw, (int) minH, ux, uy, uw, uh, image);
        }
        float minW = Math.min(tw, xrepeations * tw + 1);
        for (int yi = 0; yi < yrepeations - 1; yi++) {
            renderImage(stack, Math.max((int) (x + xrepeations * tw) - tw, x), y + yi * th, (int) minW, th, ux, uy, uw, uh, image);
        }
        renderImage(stack, Math.max((int) (x + xrepeations * tw) - tw, x), Math.max((int) (y + yrepeations * th) - th, y), (int) minW, (int) minH, ux, uy, uw, uh, image);
    }

    public static void renderGui(PoseStack stack, int x, int y, int width, int height) {
        renderContinuousTexture(stack, x,y,width,height,"textures/gui/demo_background.png",0,0,248,166,256,256,5);
    }

    public static void setScissor(int x, int y, int width, int height) {
        GL11.glScissor(x, CodeUtilities.MC.getWindow().getHeight()-y-height, width, height);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
    }

    public static void clearScissor() {
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }
}
