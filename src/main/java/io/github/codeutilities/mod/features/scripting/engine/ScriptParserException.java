package io.github.codeutilities.mod.features.scripting.engine;

public class ScriptParserException extends Exception{

    public int line;
    public ScriptParserException(int index) {
        line = index;
    }

    @Override
    public String toString() {
        return "ScriptParserException{" +
            "line=" + line +
            '}';
    }
}
