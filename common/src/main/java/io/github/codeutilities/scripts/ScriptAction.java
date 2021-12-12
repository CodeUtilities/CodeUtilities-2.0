package io.github.codeutilities.scripts;

import java.util.ArrayList;
import java.util.List;

public class ScriptAction {

    public List<ScriptAction> inner = new ArrayList<>();
    ScriptActionType type;
    List<ScriptActionArgument> args;

    public ScriptAction(ScriptActionType type, List<ScriptActionArgument> args) {
        this.type = type;
        this.args = args;
    }

    public void execute() {
        type.code.accept(args.toArray(new ScriptActionArgument[0]), () -> {
            for (ScriptAction action : inner) {
                action.execute();
            }
        });
    }

}
