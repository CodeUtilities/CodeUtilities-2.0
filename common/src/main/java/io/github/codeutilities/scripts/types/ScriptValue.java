package io.github.codeutilities.scripts.types;

import java.util.ArrayList;

public interface ScriptValue {

    default String text() {return "";}

    default double number() {return 0;}

    default ArrayList<ScriptValue> list() {return new ArrayList<>();}

}
