package io.github.codeutilities.util.templates;

import com.google.gson.JsonObject;

public class CodePotion extends CodeArgument{

    private final int amplifier, duration;
    private final String name;

    public CodePotion(int slot, String name, int amplifier, int duration) {
        super(slot, "pot");
        this.amplifier = amplifier;
        this.duration = duration;
        this.name = name;
    }

    @Override
    public JsonObject getData() {
        JsonObject data = new JsonObject();

        data.addProperty("amp", amplifier);
        data.addProperty("dur", duration);
        data.addProperty("pot", name);

        return data;
    }
}
