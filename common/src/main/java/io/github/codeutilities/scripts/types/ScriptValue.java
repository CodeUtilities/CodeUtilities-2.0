package io.github.codeutilities.scripts.types;

import java.util.ArrayList;
import java.util.HashMap;

public interface ScriptValue {

    default String text() {return "";}

    default double number() {return 0;}

    default ArrayList<ScriptValue> list() {return new ArrayList<>();}

    default HashMap<String,ScriptValue> dictionary() {return new HashMap<>();}
}
