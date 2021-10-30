package io.github.codeutilities.mod.features.scripting.engine;

import java.util.Objects;

public enum ScriptEvent implements ScriptPart{
    CHANGE_STATE("ChangeState"),
    RECEIVE_CHAT("ReceiveChat");

    String codeName;
    ScriptEvent(String codeName) {
        this.codeName = codeName;
    }

    public static ScriptEvent byName(String name) {
        for (ScriptEvent evn : values()) {
            if (Objects.equals(evn.codeName, name)) return evn;
        }
        throw new IllegalArgumentException();
    }
}
