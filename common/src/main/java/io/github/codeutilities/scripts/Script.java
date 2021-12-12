package io.github.codeutilities.scripts;

import io.github.codeutilities.event.Event;
import io.github.codeutilities.scripts.event.ScriptEvent;
import io.github.codeutilities.scripts.event.ScriptEventType;
import io.github.codeutilities.util.ChatUtil;
import io.github.codeutilities.util.FileUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Script {

    private final String name;
    private List<ScriptEvent> events;
    private final ScriptContext context = new ScriptContext();
    private String source;

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

    public void triggerEvent(ScriptEventType scriptEvent, ScriptContext ectx, Event cuEvent) {
        try {
            context.getVars().putAll(ectx.getVars());
            for (ScriptEvent event : events) {
                if (event.type == scriptEvent) {
                    event.execute(cuEvent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ChatUtil.displayClientMessage("§cError in script '" + name + "' at event '" + scriptEvent.name + "':");
            ChatUtil.displayClientMessage("§c" + e.getMessage());
        }
    }

    public String getName() {
        return name;
    }

    public String getSource() {
        return source;
    }

    public ScriptContext getContext() {
        return context;
    }
}
