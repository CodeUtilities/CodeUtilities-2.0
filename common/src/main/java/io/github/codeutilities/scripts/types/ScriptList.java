package io.github.codeutilities.scripts.types;

import java.util.ArrayList;

public record ScriptList(ArrayList<ScriptValue> value) implements ScriptValue{

    @Override
    public double number() {
        return value.size();
    }

    @Override
    public String text() {
        return value.toString();
    }

    @Override
    public ArrayList<ScriptValue> list() {
        return value;
    }

    @Override
    public ScriptValue copy() {
        ArrayList<ScriptValue> newList = new ArrayList<>();
        for(ScriptValue value : this.value){
            newList.add(value.copy());
        }
        return new ScriptList(newList);
    }

    @Override
    public String toString() {
        return text();
    }
}
