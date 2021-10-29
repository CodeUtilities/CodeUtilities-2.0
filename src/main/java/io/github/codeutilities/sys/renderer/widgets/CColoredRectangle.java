package io.github.codeutilities.sys.renderer.widgets;

import io.github.codeutilities.sys.renderer.RenderUtil;
import io.github.cottonmc.cotton.gui.client.LibGui;
import io.github.cottonmc.cotton.gui.impl.client.LibGuiClient;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;

public class CColoredRectangle extends WWidget {

    private Color color;
    private Color darkmodeColor;

    public CColoredRectangle(Color color, Color darkmodecolor){
        this.color = color;
        this.darkmodeColor = darkmodecolor;
    }

    @Override
    public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
        DrawableHelper.fill(matrices, x, y, x+this.width, y+this.height, LibGui.isDarkMode() ? this.darkmodeColor.getRGB() : this.color.getRGB());
    }

    @Override
    public boolean canResize() {
        return true;
    }

    public void setColor(Color color, Color darkmodecolor){
        this.color = color;
        this.darkmodeColor = darkmodecolor;
    }
}
