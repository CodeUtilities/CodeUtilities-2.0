package io.github.codeutilities.scripts.event;

public enum ScriptEventType {

    SEND_CHAT("SendChat","Runs when the player sends a message into the chat. Sets the message variable to the message sent.");

    public final String name;
    public final String description;

    ScriptEventType(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public static boolean exists(String name) {
        for(ScriptEventType event : values()) {
            if(event.name.equals(name)) {
                return true;
            }
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
