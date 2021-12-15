package io.github.codeutilities.util.templates;

import com.google.gson.JsonObject;

public class CodeNumber extends CodeArgument {
    private final String name;

    public CodeNumber(int slot, String name) {
        super(slot, "num");
        this.name = name;
    }

    @Override
    public JsonObject getData() {
        JsonObject data = new JsonObject();
        data.addProperty("name", name);
        return data;
    }
}
