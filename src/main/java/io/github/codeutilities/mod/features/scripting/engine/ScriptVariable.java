package io.github.codeutilities.mod.features.scripting.engine;

public class ScriptVariable {

    ScriptContext ctx;
    String name;

    public Object get() {
        return ctx.getVar(name);
    }

    public void set(Object value) {
        ctx.setVar(name,value);
    }

    public ScriptVariable(String name, ScriptContext ctx) {
        this.ctx = ctx;
        this.name = name;
    }
}
