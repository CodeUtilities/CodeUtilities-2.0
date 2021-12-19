package io.github.codeutilities.scripts.types;

public class ScriptUnknownValue implements ScriptValue {

    @Override
    public String toString() {
        return "Unknown";
    }

    @Override
    public ScriptValue copy() {
        return this;
    }
}
