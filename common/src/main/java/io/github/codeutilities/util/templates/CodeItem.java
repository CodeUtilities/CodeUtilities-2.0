package io.github.codeutilities.util.templates;

import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class CodeItem extends CodeArgument {

    private final ItemStack item;

    public CodeItem(int slot, ItemStack item) {
        super(slot, "item");
        this.item = item;
    }

    @Override
    public JsonObject getData() {
        JsonObject json = new JsonObject();

        CompoundTag t = item.save(new CompoundTag());
        json.addProperty("item", t.getAsString());

        return json;
    }
}
