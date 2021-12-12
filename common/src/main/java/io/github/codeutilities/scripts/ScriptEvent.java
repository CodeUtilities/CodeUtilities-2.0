package io.github.codeutilities.scripts;

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

    public void execute() {
        for(ScriptAction action : actions) {
            action.execute();
        }
    }
}
