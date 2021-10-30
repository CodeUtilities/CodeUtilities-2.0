package io.github.codeutilities.mod.features.scripting.engine;

public class ScriptArguments {

    public static final ScriptArguments EMPTY = new ScriptArguments(new Object[]{});
    Object[] args;

    public ScriptArguments(Object[] args) {
        this.args = args;
    }

    public ScriptVariable getVar(int i) {
        return (ScriptVariable) args[i];
    }

    public Object get(int i) {
        if (args[i] instanceof ScriptVariable var) {
            return var.get();
        }
        return args[i];
    }

    public int amount() {
        return args.length;
    }

    public String getString(int i) {
        return String.valueOf(get(i));
    }

    public String fullString() {
        StringBuilder msg = new StringBuilder();
        for (int i = 0; i < amount(); i++) {
            msg.append(getString(i));
        }
        return msg.toString();
    }
}
