package io.github.codeutilities.menus;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.codeutilities.menus.sys.CPanel;
import io.github.codeutilities.menus.sys.CScriptTextField;
import io.github.codeutilities.menus.sys.CText;
import io.github.codeutilities.scripts.Script;
import io.github.codeutilities.scripts.ScriptHandler;
import io.github.codeutilities.scripts.ScriptParser;
import io.github.codeutilities.util.ChatUtil;
import io.github.codeutilities.util.FileUtil;
import java.io.File;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;

public class ScriptEditorMenu extends Screen {

    final CPanel panel;
    Script script;
    CScriptTextField field;

    public ScriptEditorMenu(String name) {
        super(new TextComponent("Script Editor"));
        for (Script s : ScriptHandler.scripts) {
            if (s.getName().equalsIgnoreCase(name)) {
                script = s;
                break;
            }
        }
        if (script == null) {
            throw new IllegalStateException("Script not found!");
        }
        panel = new CPanel(120, 110);

        field = new CScriptTextField(script.getSource(), 2, 2, 116, 96, true);
        CText errorMsg = new CText( 60, 105, "§aValid", 0, 1, true, true);

        field.setChangedListener(() -> {
            try {
                ScriptParser.parseFile(field.getText(),script);
                field.textColor = 0xFFFFFFFF;
                errorMsg.setText("§aScript Valid");
            } catch (Exception err) {
                field.textColor = 0xFFFF3333;
                errorMsg.setText("§c" + err.getMessage());
            }
        });

        panel.add(errorMsg);
        panel.add(field);
    }

    @Override
    public void onClose() {
        File f = FileUtil.cuFolder("Scripts").resolve(script.getName()).toFile();
        String text = field.getText();

        try {
            FileUtil.writeFile(f, text);
        } catch (Exception err) {
            err.printStackTrace();
        }

        ScriptHandler.load();
        ChatUtil.displayClientMessage("§aReloaded scripts!");
        super.onClose();
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float tickDelta) {
        renderBackground(stack);
        panel.render(stack, mouseX, mouseY, tickDelta);
        super.render(stack, mouseX, mouseY, tickDelta);
    }

    @Override
    public boolean charTyped(char c, int i) {
        panel.charTyped(c, i);
        return super.charTyped(c, i);
    }

    @Override
    public boolean keyPressed(int i, int j, int k) {
        panel.keyPressed(i, j, k);
        return super.keyPressed(i, j, k);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        panel.mouseClicked(mouseX, mouseY, button);
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double d, double e, double f) {
        panel.mouseScrolled(d, e, f);
        return super.mouseScrolled(d, e, f);
    }
}
