package io.github.codeutilities.util.templates;

import com.google.gson.JsonObject;

public class CodeVector extends CodeArgument {

    private final int x, y, z;

    public CodeVector(int slot, int x, int y, int z) {
        super(slot, "vec");
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public JsonObject getData() {
        JsonObject data = new JsonObject();
        data.addProperty("x", x);
        data.addProperty("y", y);
        data.addProperty("z", z);
        return data;
    }
}
