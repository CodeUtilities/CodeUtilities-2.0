package io.github.codeutilities.mod.features.scripting.engine;

import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.sys.file.FileUtil;
import io.github.codeutilities.sys.file.ILoader;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.Level;

public class ScriptHandler implements ILoader {

    public static List<Script> scripts = new ArrayList<>();

    public static void reload() {
        scripts = new ArrayList<>();
        new ScriptHandler().load();
    }

    public static void triggerEvent(ScriptEvent evn) {
        triggerEvent(evn,new ScriptContext());
    }
    public static void triggerEvent(ScriptEvent evn, ScriptContext ctx) {
        for (Script s : scripts) {
            s.triggerEvent(evn,ctx);
        }
    }

    @Override
    public void load() {
        Path modFolder = FabricLoader.getInstance().getGameDir().resolve("CodeUtilities");
        Path scriptsPath = modFolder.resolve("Scripts");
        File scriptsFolder = scriptsPath.toFile();

        if (!scriptsFolder.exists()) {
            scriptsFolder.mkdir();
        }

        File[] scriptFiles = scriptsFolder.listFiles();
        if (scriptFiles != null) {
            CodeUtilities.log(Level.INFO, "Loading scripts...");

            for (File file : scriptFiles) {
                try {
                    String name = file.getName();
                    String source = FileUtil.readFile(String.valueOf(file.toPath()), Charset.defaultCharset());
                    scripts.add(Script.of(name,source,file));
                } catch (Exception e) {
                    System.err.println("Error loading script: " + file.getName());
                    e.printStackTrace();
                }
                CodeUtilities.log(Level.INFO, "Loaded scripts!");
            }
        }
    }
}