package io.github.codeutilities.scripts.types;

public record ScriptNumber(double value) implements ScriptValue {

    @Override
    public String text() {
        if (value % 1 == 0) {
            return String.valueOf((int) value);
        }
        return String.valueOf(value);
    }

    @Override
    public double number() {
        return value;
    }
}
