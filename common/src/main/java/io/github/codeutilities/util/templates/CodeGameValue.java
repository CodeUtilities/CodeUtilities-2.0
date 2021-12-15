package io.github.codeutilities.util.templates;

import com.google.gson.JsonObject;

public class CodeGameValue extends CodeArgument {

    private final String name, target;

    public CodeGameValue(int slot, String name, String target) {
        super(slot, "g_val");
        this.name = name;
        this.target = target;
    }
    public CodeGameValue(int slot, String name) {
        super(slot, "g_val");
        this.name = name;
        this.target = "Default";
    }

    @Override
    public JsonObject getData() {
        JsonObject data = new JsonObject();
        data.addProperty("type", name);
        data.addProperty("target", target);
        return data;
    }
}
