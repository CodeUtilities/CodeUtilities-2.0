package io.github.codeutilities.scripts.event;

import io.github.codeutilities.event.Event;
import io.github.codeutilities.scripts.action.ScriptAction;

import java.util.ArrayList;
import java.util.List;

public class ScriptEvent {

    public ScriptEventType type = null;
    public List<ScriptAction> actions = new ArrayList<>();

    public ScriptEvent(String name) {
        for (ScriptEventType type : ScriptEventType.values()) {
            if (type.name.equals(name)) {
                this.type = type;
            }
        }
    }

    public void execute(Event event) {
        for(ScriptAction action : actions) {
            action.execute(event);
        }
    }
}
