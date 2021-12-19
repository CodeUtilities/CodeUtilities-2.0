package io.github.codeutilities.util.templates;

public enum CodeBlock {

    PLAYER_ACTION("player_action"),
    IF_PLAYER("if_player"),
    CALL_FUNCTION("call_func","data"),
    FUNCTION("func","data"),
    ENTITY_EVENT("entity_event"),
    SET_VARIABLE("set_var"),
    IF_ENTITY("if_entity"),
    ENTITY_ACTION("entity_action"),
    IF_VARIABLE("if_var"),
    SELECT_OBJECT("select_obj"),
    PLAYER_EVENT("event"),
    GAME_ACTION("game_action"),
    ELSE("else"),
    START_PROCESS("start_process","data"),
    PROCESS("process","data");

    private final String name;
    private final String actionPath;

    CodeBlock(String name, String action) {
        this.name = name;
        this.actionPath = action;
    }

    CodeBlock(String name) {
        this(name, "action");
    }

    public String getName() {
        return name;
    }

    public String getActionPath() {
        return actionPath;
    }
}
