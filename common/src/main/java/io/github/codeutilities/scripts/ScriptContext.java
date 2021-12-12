package io.github.codeutilities.scripts;

import java.util.HashMap;

public class ScriptContext {

    private final HashMap<String,Object> vars = new HashMap<>();

    public void setVar(String name, Object value){
        vars.put(name,value);
    }

    public Object getVar(String name){
        return vars.get(name);
    }

    public boolean hasVar(String name){
        return vars.containsKey(name);
    }

    public HashMap<String, Object> getVars() {
        return vars;
    }
}
