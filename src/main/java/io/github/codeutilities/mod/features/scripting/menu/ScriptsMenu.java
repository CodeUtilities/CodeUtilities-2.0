package io.github.codeutilities.mod.features.scripting.menu;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.mod.features.scripting.engine.Script;
import io.github.codeutilities.mod.features.scripting.engine.ScriptHandler;
import io.github.codeutilities.sys.renderer.IMenu;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.WScrollPanel;
import io.github.cottonmc.cotton.gui.widget.WText;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import java.awt.Dialog;
import java.awt.FileDialog;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import net.fabricmc.fabric.api.util.TriState;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.LiteralText;
import org.apache.commons.io.FileUtils;

public class ScriptsMenu extends LightweightGuiDescription implements IMenu {

    @Override
    public void open(String... args) throws CommandSyntaxException {
        ScriptHandler.reload();
        WPlainPanel root = new WPlainPanel();
        root.setInsets(Insets.ROOT_PANEL);
        root.setSize(200, 200);

        WPlainPanel scriptList = new WPlainPanel();

        int yoffset = 0;
        for (Script script : ScriptHandler.scripts) {
            WText title = new WText(new LiteralText(script.source.equals("#Error") ? "§c" + script.name : script.name));
            WButton editBtn = new WButton(new LiteralText("§l⚒"));
            scriptList.add(title, 0, 5 + yoffset * 20, 9999, 0);
            scriptList.add(editBtn, 170, yoffset * 20, 20, 20);

            editBtn.setOnClick(() -> {
                ScriptEditorMenu editorMenu = new ScriptEditorMenu(script);
                editorMenu.scheduleOpenGui(editorMenu);
            });

            yoffset++;
        }

        WScrollPanel scriptListScroll = new WScrollPanel(scriptList);
        scriptListScroll.setScrollingVertically(TriState.TRUE);
        scriptListScroll.setScrollingHorizontally(TriState.FALSE);
        root.add(scriptListScroll, 0, 0, 200, 180);

        WButton createBtn = new WButton(new LiteralText("Create"));
        WButton importBtn = new WButton(new LiteralText("Import"));

        root.add(createBtn, 0, 180, 100, 0);
        root.add(importBtn, 100, 180, 100, 0);

        createBtn.setOnClick(() -> {
            try {
                Path modFolder = FabricLoader.getInstance().getGameDir().resolve("CodeUtilities");
                Path scriptsPath = modFolder.resolve("Scripts");
                int num = 2;
                Path destinationPath = scriptsPath.resolve("New Script");
                while (destinationPath.toFile().exists()) {
                    destinationPath = scriptsPath.resolve("New Script (" + (num++) + ")");
                }
                destinationPath.toFile().createNewFile();

                File destFile = destinationPath.toFile();
                ScriptEditorMenu menu = new ScriptEditorMenu(Script.of(destFile.getName(), "", destFile));
                menu.scheduleOpenGui(menu);
            } catch (Exception err) {
                err.printStackTrace();
            }
        });

        importBtn.setOnClick(() -> CodeUtilities.EXECUTOR.submit(() -> {
            FileDialog fd = new FileDialog((Dialog) null, "Choose a script file", FileDialog.LOAD);
            fd.setVisible(true);
            File[] f = fd.getFiles();
            if (f.length > 0) {
                Path modFolder = FabricLoader.getInstance().getGameDir().resolve("CodeUtilities");
                Path scriptsPath = modFolder.resolve("Scripts");
                int num = 2;
                Path destinationPath = scriptsPath.resolve(f[0].getName());
                while (destinationPath.toFile().exists()) {
                    destinationPath = scriptsPath.resolve(f[0].getName() + " (" + (num++) + ")");
                }
                try {
                    FileUtils.copyFile(f[0], destinationPath.toFile());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                scheduleOpenGui(this);
            }
        }));

        setRootPanel(root);
    }
}
