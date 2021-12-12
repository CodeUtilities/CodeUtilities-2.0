package io.github.codeutilities.scripts;

import io.github.codeutilities.util.ChatUtil;
import io.github.codeutilities.util.FileUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Script {

    String name;
    List<ScriptEvent> events;
    ScriptContext context = new ScriptContext();
    String source;

    public Script(String name, File source) {
        try {
            this.source = FileUtil.readFile(source);
        } catch (Exception err) {
            err.printStackTrace();
            this.source = "Error Loading from file!";
        }
        this.name = name;
        try {
            events = ScriptParser.parseFile(this.source,this);
        } catch (Exception err) {
            events = new ArrayList<>();
        }
    }

    public void triggerEvent(ScriptEventType evn, ScriptContext ectx) {
        try {
            context.vars.putAll(ectx.vars);
            for (ScriptEvent event : events) {
                if (event.type == evn) {
                    event.execute();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ChatUtil.displayClientMessage("§cError in script '" + name + "' at event '" + evn.name + "':");
            ChatUtil.displayClientMessage("§c" + e.getMessage());
        }
    }

    public String getName() {
        return name;
    }

    public String getSource() {
        return source;
    }
}
