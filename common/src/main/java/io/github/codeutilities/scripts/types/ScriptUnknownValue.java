package io.github.codeutilities.scripts.types;

public class ScriptUnknownValue implements ScriptValue{

    @Override
    public String text() {
        return "";
    }

    @Override
    public double number() {
        return 0;
    }
}
