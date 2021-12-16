package io.github.codeutilities.scripts;

import io.github.codeutilities.scripts.types.ScriptUnknownValue;
import io.github.codeutilities.scripts.types.ScriptValue;
import java.util.HashMap;

public class ScriptContext {

    private final HashMap<String, ScriptValue> vars = new HashMap<>();

    public void setVar(String name, ScriptValue value) {
        vars.put(name, value);
    }

    public ScriptValue getVar(String name) {
        if (hasVar(name)) {
            return vars.get(name);
        }
        return new ScriptUnknownValue();
    }

    public boolean hasVar(String name){
        return vars.containsKey(name);
    }

    public HashMap<String, ScriptValue> getVars() {
        return vars;
    }
}
