package io.github.codeutilities.util.templates;

import com.google.gson.JsonObject;

public class CodeLocation extends CodeArgument{

    private final int x, y, z, pitch, yaw;
    private final boolean isBlock;

    public CodeLocation(int slot, int x, int y, int z, int pitch, int yaw, boolean isBlock) {
        super(slot, "loc");
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
        this.isBlock = isBlock;
    }

    @Override
    public JsonObject getData() {
        JsonObject data = new JsonObject();
        JsonObject loc = new JsonObject();
        loc.addProperty("x", x);
        loc.addProperty("y", y);
        loc.addProperty("z", z);
        loc.addProperty("pitch", pitch);
        loc.addProperty("yaw", yaw);
        data.add("loc", loc);
        data.addProperty("isBlock", isBlock);

        return data;
    }

}
