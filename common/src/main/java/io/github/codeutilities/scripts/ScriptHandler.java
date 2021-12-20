package io.github.codeutilities.scripts;

import io.github.codeutilities.event.Event;
import io.github.codeutilities.scripts.event.ScriptEventType;
import io.github.codeutilities.util.FileUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ScriptHandler {

    public static final Logger LOGGER = LogManager.getLogger("CuScripts");
    public static List<Script> scripts = new ArrayList<>();

    public static void init() {
        scripts.clear();
        LOGGER.info("Loading scripts...");
        File folder = FileUtil.cuFolder("Scripts").toFile();
        if (!folder.exists()) {
            folder.mkdirs();
        }
        for (File file : folder.listFiles()) {
            if (file.isFile()) {
                String name = file.getName();
                if (name.endsWith(".cus")) {
                    scripts.add(new Script(name, file));
                } else {
                    LOGGER.warn("Ignored file: " + file.getName() + ", Does not end with .cus (CodeUtilitiesScript)");
                }
            }
        }
        LOGGER.info("Loaded scripts!");
    }

    public static void triggerEvent(ScriptEventType scriptEvent, Event cuEvent) {
        triggerEvent(scriptEvent, new ScriptContext(), cuEvent);
    }

    public static void triggerEvent(ScriptEventType scriptEvent, ScriptContext ctx, Event cuEvent) {
        for (Script script : scripts) {
            script.triggerEvent(scriptEvent, ctx, cuEvent);
        }
    }

    public static boolean createScript(String name) {
        try {
            return FileUtil.cuFolder("Scripts").resolve(name + ".cus").toFile().createNewFile();
        } catch (Exception err) {
            err.printStackTrace();
            return false;
        }
    }

    public static boolean deleteScript(String name) {
        try {
            return FileUtil.cuFolder("Scripts").resolve(name).toFile().delete();
        } catch (Exception err) {
            err.printStackTrace();
            return false;
        }
    }
}
