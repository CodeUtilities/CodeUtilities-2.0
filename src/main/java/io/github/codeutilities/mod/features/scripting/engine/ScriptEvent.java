package io.github.codeutilities.mod.features.scripting.engine;

import java.util.Objects;

public enum ScriptEvent implements ScriptPart{
    CHANGE_STATE("ChangeState","Runs whenever the mode the player is in changes"),
    RECEIVE_CHAT("ReceiveChat","Runs for every received chat message");

    public String codeName;
    public String description;
    ScriptEvent(String codeName, String description) {
        this.codeName = codeName;
        this.description = description;
    }

    public static ScriptEvent byName(String name) {
        for (ScriptEvent evn : values()) {
            if (Objects.equals(evn.codeName, name)) return evn;
        }
        throw new IllegalArgumentException();
    }
}
