package io.github.codeutilities.mod.features.scripting.menu;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.codeutilities.mod.features.scripting.engine.Script;
import io.github.codeutilities.sys.renderer.IMenu;
import io.github.codeutilities.sys.renderer.widgets.CCodeTextField;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.WTextField;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;
import net.minecraft.text.LiteralText;

public class ScriptEditorMenu extends LightweightGuiDescription implements IMenu {

    Script script;
    WButton saveBtn;

    public ScriptEditorMenu(Script script) {
        this.script = script;
    }

    @Override
    public void open(String... args) throws CommandSyntaxException {
        WPlainPanel root = new WPlainPanel();
        root.setSize(200, 200);
        root.setInsets(Insets.ROOT_PANEL);

        WTextField name = new WTextField();
        name.setMaxLength(50);
        name.setText(script.name);
        root.add(name, 0, 0, 200, 0);

        saveBtn = new WButton(new LiteralText("Save"));

        CCodeTextField source = new CCodeTextField(saveBtn);
        source.setMaxLength(Integer.MAX_VALUE);
        source.setText(script.source);
        root.add(source, 0, 25, 200, 150);

        WButton cancelBtn = new WButton(new LiteralText("Cancel"));

        root.add(cancelBtn, 0, 180, 100, 0);
        root.add(saveBtn, 100, 180, 100, 0);

        saveBtn.setOnClick(() -> {
            try {
                File f = script.file;
                if (!Objects.equals(name.getText(), script.name)) {
                    script.file.delete();
                    f = f.toPath().resolveSibling(name.getText()).toFile();
                }
                Files.write(f.toPath(), source.getText().getBytes());

                ScriptsMenu menu = new ScriptsMenu();
                menu.scheduleOpenGui(menu);
            } catch (IOException e) {
                e.printStackTrace();
                saveBtn.setLabel(new LiteralText("Error"));
            }
        });

        cancelBtn.setOnClick(() -> {
            ScriptsMenu menu = new ScriptsMenu();
            menu.scheduleOpenGui(menu);
        });

        setRootPanel(root);
    }
}
