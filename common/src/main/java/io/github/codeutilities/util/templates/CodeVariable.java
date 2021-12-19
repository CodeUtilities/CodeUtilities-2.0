package io.github.codeutilities.util.templates;

import com.google.gson.JsonObject;

public class CodeVariable extends CodeArgument{

    private final String name;
    private final Scope scope;

    public CodeVariable(int slot, String name, Scope scope) {
        super(slot, "var");
        this.name = name;
        this.scope = scope;
    }

    @Override
    public JsonObject getData() {
        JsonObject data = new JsonObject();
        data.addProperty("name", name);
        data.addProperty("scope", scope.getId());
        return data;
    }

    public enum Scope {
        GAME("unsaved"),SAVE("saved"),LOCAL("local");

        private final String id;
        Scope(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }

}
