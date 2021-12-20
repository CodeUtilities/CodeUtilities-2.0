package io.github.codeutilities.scripts.action;

import io.github.codeutilities.event.Event;
import io.github.codeutilities.scripts.Script;
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

    public void execute(Event event, Script script) {
        type.getCode().accept(new ScriptActionInfo(
            args.toArray(new ScriptActionArgument[0]),
            () -> {
                for (ScriptAction action : inner) {
                    action.execute(event,script);
                }
            },
            event,
            script
        ));
    }

    public ScriptActionType getType() {
        return type;
    }
}
