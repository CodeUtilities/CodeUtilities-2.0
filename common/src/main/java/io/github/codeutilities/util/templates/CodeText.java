package io.github.codeutilities.util.templates;

import com.google.gson.JsonObject;

public class CodeText extends CodeArgument{

    private final String text;

    public CodeText(int slot, String text) {
        super(slot, "txt");
        this.text = text;
    }

    @Override
    public JsonObject getData() {
        JsonObject data = new JsonObject();
        data.addProperty("name", text);
        return data;
    }

}
