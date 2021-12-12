package io.github.codeutilities.scripts.action;

import io.github.codeutilities.scripts.Script;

public class ScriptActionArgument {

    ScriptActionArgumentType type;
    Object value;
    Script script;

    public ScriptActionArgument(ScriptActionArgumentType type, Object value, Script script) {
        this.type = type;
        this.value = value;
        this.script = script;
    }

    public String getText() {
        return String.valueOf(get());
    }

    public double getNumber() {
        Object value = get();
        if (value == null) return 0;
        return (double) value;
    }

    public Object get() {
        if (type == ScriptActionArgumentType.TEXT || type == ScriptActionArgumentType.NUMBER) {
            return value;
        }
        return script.getContext().getVar(value.toString());
    }

    public void set(Object value) {
        if (type == ScriptActionArgumentType.VARIABLE) {
            script.getContext().setVar(this.value.toString(), value);
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
