package com.lolpop.templateutilities.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class TemplateNBT {
    public static void setTemplateNBT(ItemStack stack, String name, String author, String template) {
        try {
            byte[] b64 = GzFormat.encryptBase64(GzFormat.compress(template.getBytes()));
            String exported = new String(b64);
            final CompoundNBT nbt = new CompoundNBT();

            nbt.putString("author", author);
            nbt.putString("name", name);
            nbt.putInt("version", 1);
            nbt.putString("code", exported);

            final CompoundNBT itemNbt = new CompoundNBT();
            final CompoundNBT publicBukkitNbt = new CompoundNBT();

            publicBukkitNbt.putString("hypercube:codetemplatedata", nbt.toString());
            itemNbt.put("PublicBukkitValues", publicBukkitNbt);

            stack.setTag(itemNbt);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void setTemplateNBTGZIP(ItemStack stack, String name, String author, String template) {
        try {
            final CompoundNBT nbt = new CompoundNBT();

            nbt.putString("author", author);
            nbt.putString("name", name);
            nbt.putInt("version", 1);
            nbt.putString("code", template);

            final CompoundNBT itemNbt = new CompoundNBT();
            final CompoundNBT publicBukkitNbt = new CompoundNBT();

            publicBukkitNbt.putString("hypercube:codetemplatedata", nbt.toString());
            itemNbt.put("PublicBukkitValues", publicBukkitNbt);

            stack.setTag(itemNbt);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
