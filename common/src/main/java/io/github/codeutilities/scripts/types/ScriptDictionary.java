package io.github.codeutilities.scripts.types;

import java.util.HashMap;

public record ScriptDictionary(HashMap<String,ScriptValue> value) implements ScriptValue{

    @Override
    public HashMap<String, ScriptValue> dictionary() {
        return value;
    }

    @Override
    public String text() {
        return value.toString();
    }

    @Override
    public double number() {
        return value.size();
    }

    @Override
    public String toString() {
        return text();
    }
}
