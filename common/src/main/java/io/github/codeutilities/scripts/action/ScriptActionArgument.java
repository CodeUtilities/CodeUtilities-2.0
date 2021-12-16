package io.github.codeutilities.scripts.action;

import io.github.codeutilities.scripts.Script;
import io.github.codeutilities.scripts.types.ScriptValue;

public class ScriptActionArgument {

    ScriptActionArgumentType type;
    ScriptValue value;
    Script script;

    public ScriptActionArgument(ScriptActionArgumentType type, ScriptValue value, Script script) {
        this.type = type;
        this.value = value;
        this.script = script;
    }

    public ScriptValue get() {
        if (type == ScriptActionArgumentType.TEXT || type == ScriptActionArgumentType.NUMBER) {
            return value;
        }
        return script.getContext().getVar(value.text());
    }

    public void set(ScriptValue value) {
        if (type == ScriptActionArgumentType.VARIABLE) {
            script.getContext().setVar(this.value.text(), value);
        } else {
            throw new IllegalArgumentException("Cannot set value! (Not a variable)");
        }
    }

    public enum ScriptActionArgumentType {
        TEXT,
        NUMBER,
        VARIABLE
    }

}
