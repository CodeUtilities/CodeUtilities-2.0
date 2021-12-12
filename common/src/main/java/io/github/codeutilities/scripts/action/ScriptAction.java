package io.github.codeutilities.scripts.action;

import io.github.codeutilities.event.Event;
import java.util.ArrayList;
import java.util.List;

public class ScriptAction {

    public List<ScriptAction> inner = new ArrayList<>();
    private final ScriptActionType type;
    List<ScriptActionArgument> args;

    public ScriptAction(ScriptActionType type, List<ScriptActionArgument> args) {
        this.type = type;
        this.args = args;
    }

    public void execute(Event event) {
        type.getCode().accept(args.toArray(new ScriptActionArgument[0]), () -> {
            for (ScriptAction action : inner) {
                action.execute(event);
            }
        }, event);
    }

    public ScriptActionType getType() {
        return type;
    }
}
