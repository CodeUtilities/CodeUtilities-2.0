package io.github.codeutilities.mod.features.scripting.engine;

import java.util.HashMap;

public class ScriptContext {

    HashMap<String,Object> vars = new HashMap<>();

    public void setVar(String name, Object value) {
        vars.put(name,value);
    }

    public Object getVar(String name) {
        return vars.get(name);
    }
}
