package io.github.codeutilities.util.templates;

import com.google.gson.JsonObject;

public class CodeSound extends CodeArgument{

    private final String name;
    private final double volume, pitch;

    public CodeSound(int slot, String name, double volume, double pitch) {
        super(slot,"snd");
        this.name = name;
        this.volume = volume;
        this.pitch = pitch;
    }

    @Override
    public JsonObject getData() {
        JsonObject data = new JsonObject();
        data.addProperty("vol", volume);
        data.addProperty("pitch", pitch);
        data.addProperty("sound", name);
        return data;
    }
}
