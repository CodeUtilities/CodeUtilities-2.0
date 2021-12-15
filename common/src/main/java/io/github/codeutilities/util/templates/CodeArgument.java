package io.github.codeutilities.util.templates;

import com.google.gson.JsonObject;

public abstract class CodeArgument {

    private final int slot;
    private final String id;

    public CodeArgument(int slot, String id) {
        this.slot = slot;
        this.id = id;
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("slot", slot);
        JsonObject item = new JsonObject();
        item.addProperty("id", id);
        item.add("data",getData());
        json.add("item", item);
        return json;
    }

    public abstract JsonObject getData();
}
