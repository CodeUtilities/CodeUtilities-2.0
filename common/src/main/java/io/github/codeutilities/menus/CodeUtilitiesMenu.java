package io.github.codeutilities.menus;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.menus.sys.CButton;
import io.github.codeutilities.menus.sys.CImage;
import io.github.codeutilities.menus.sys.CPanel;
import io.github.codeutilities.menus.sys.CText;
import java.net.URL;
import net.minecraft.Util;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;

public class CodeUtilitiesMenu extends Screen {

    final CPanel panel;

    public CodeUtilitiesMenu() {
        super(new TextComponent("CodeUtilities"));
        panel = new CPanel(120, 70);

        String version = "3.0.0-BETA";//TODO: Get version from config
        String env = CodeUtilities.platform.displayName;

        panel.add(new CImage(0, -5, 60, 60, "codeutilities:icon.png"));

        panel.add(new CText(30, 55, "CodeUtilities", 0x444444, 0.5f, true, false));
        panel.add(new CText(30, 60, env, 0x444444, 0.5f, true, false));
        panel.add(new CText(30, 65, version, 0x444444, 0.5f, true, false));

        panel.add(new CButton(60, 8, 55, 11, "Features", () -> {
        }));
        panel.add(new CButton(60, 23, 55, 11, "Options", () -> {
        }));
        panel.add(new CButton(60, 38, 55, 11, "Contributors", () -> {
        }));
        panel.add(new CButton(60, 53, 55, 11, "Bug Report", () -> {
            try {
                Util.getPlatform().openUrl(new URL("https://discord.gg/WY6tPFE"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float tickDelta) {
        renderBackground(poseStack);
        panel.render(poseStack, mouseX, mouseY, tickDelta);
        super.render(poseStack, mouseX, mouseY, tickDelta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        panel.mouseClicked(mouseX, mouseY, button);
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
