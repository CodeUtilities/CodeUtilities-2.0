package io.github.codeutilities.util.templates;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.zip.GZIPOutputStream;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;

public class Template {

    private final List<CodeElement> blocks = new ArrayList<>();
    private final TextComponent name;
    private final String author;

    public Template(TextComponent name, String author) {
        this.name = name;
        this.author = author;
    }

    public void add(CodeElement block) {
        blocks.add(block);
    }

    private JsonObject toJson() {
        JsonObject json = new JsonObject();
        JsonArray blocks = new JsonArray();
        for (CodeElement block : this.blocks) {
            blocks.add(block.toJson());
        }
        json.add("blocks", blocks);
        return json;
    }

    public ItemStack toItem(ItemStack stack) {
        CompoundTag publicBukkitValues = stack.getOrCreateTagElement("PublicBukkitValues");
        stack.setHoverName(name);

        JsonObject templateData = new JsonObject();
        templateData.addProperty("author", author);
        templateData.addProperty("name", name.getString());
        templateData.addProperty("code", toBase64());
        templateData.addProperty("version", 1);

        publicBukkitValues.putString("hypercube:codetemplatedata", templateData.toString());

        stack.addTagElement("PublicBukkitValues", publicBukkitValues);
        return stack;
    }

    private String toBase64() {
        try {
            String json = toJson().toString();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            GZIPOutputStream gzip = new GZIPOutputStream(outputStream);
            gzip.write(json.getBytes());
            gzip.close();
            byte[] compressed = outputStream.toByteArray();
            byte[] base64 = Base64.getEncoder().encode(compressed);
            return new String(base64);
        } catch (Exception err) {
            err.printStackTrace();
            return "Error while compressing template";
        }
    }
    
}
