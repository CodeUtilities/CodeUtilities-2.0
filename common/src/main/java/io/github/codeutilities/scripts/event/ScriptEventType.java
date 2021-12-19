package io.github.codeutilities.scripts.event;

public enum ScriptEventType {

    SEND_CHAT("SendChat","Runs when the player sends a message into the chat. Sets the message variable to the message sent."),
    RECEIVE_CHAT("ReceiveChat","Runs when the player receives a message from the server. Sets the message variable to the message received."),
    KEY_PRESS("KeyPress","Runs when the player presses a key. Sets the key variable to the key pressed."),
    KEY_RELEASE("KeyRelease","Runs when the player releases a key. Sets the key variable to the key released."),
    TICK("Tick","Runs every tick."),
    INIT("Init","Runs when the script is loaded."),
    REGISTER_CMDS("RegisterCmds","Runs whenever commands are supposed to be registered."),
    SEND_COMMAND("SendCommand","Runs when the player sends a command. Sets the command variable to the entire command sent with the slash removed. Only meant for commands that are registered via scripts."),
    FINALIZE("Finalize","Runs when the script is unloaded."),
    RENDER_GUI("RenderGui", "Runs when a gui is being rendered. Sets screenWidth and screenHeight");

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
