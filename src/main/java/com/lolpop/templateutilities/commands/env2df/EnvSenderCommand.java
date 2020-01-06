package com.lolpop.templateutilities.commands.env2df;

import com.lolpop.templateutilities.clientcommands.LolpopClientCommandSource;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.StringTextComponent;

public class EnvSenderCommand
{
    public static int execute(Minecraft mc, CommandContext<LolpopClientCommandSource> ctx)
    {
        final CompoundNBT nbt = new CompoundNBT();

        nbt.putString("author", "Lolpop");
        nbt.putString("name", "Environment Loader");
        nbt.putInt("version", 1);
        nbt.putString("code", "H4sIAAAAAAAAAMVX0W6bMBT9FcvSpEXjIbTbHpj60HWr1mmrJjXbHqoqco1LrRobmUvWqOLfa0xSSIgJJGF9SoDrc889515jnvCtUPQhxcH1E+YhDspr7C1+A3yXSWouiY5MkIkBFi+izT97p1w1BRKZuJAAKe6qBLiS5sk5ESkzD4rHAb5I0TcehkwWkHQREs4liTldT5rnHk6FAhwcfcxv8iU2FooYgBnOPQfllMF0RvR21mXQkrIhwQo2xaXJTFXCbDZKRI3L2KbdF8W3Bb0o8F1xOWGPMFRNgqfwn+paRwFTVQPlU93cVSmuEsGhXQvNEkZgRyksyGG06KBqUw/3pEx0Vg3KqRDqH/phMqCzeyIjltYn5lzpr4TeNzVpzExjSaWqJvSBWXO4ZtQsMVTsYMLc1rKEHKYlD+hDF6g+RlwqdJUQaiUv3Sj6Ef0hImPoJ9MRl1HdDdutTV3cXgw56gRA89sMLPtXEXbjxHt7Tfyeu5+ivbSQWdwEecNlyB7fVvJ6/qhVho4oR6MVZXZEOa6jHPfo9V9mBTpTSodcEqj2n+oWmhTbQd0vBmZ3sgFpr7ZfWThU++vIr5mdyZTMWHgIu9+P3O/vE3c5ptnE1HGIqh1qLr6AuixSO4ESQeZMTxc5d1PH/vRTZ4eJ8tdtl+FnW8RQnkvgwra3e8ZXGL1r8YvfDcykU/t9GI/dgqIT1OM1LpWOX1P5TvW2VNs2W0qCVmI7+Y05/VaaLec0XnwwvRwNeMzQb8mhvkX+JRw28HRvjXZBm6dUqPIzaqOpW+OXZ7mb/BkZJVR18g0AAA==");

        final CompoundNBT itemNbt = new CompoundNBT();
        final CompoundNBT publicBukkitNbt = new CompoundNBT();

        publicBukkitNbt.putString("hypercube:codetemplatedata", nbt.toString());
        itemNbt.put("PublicBukkitValues", publicBukkitNbt);

        ItemStack stack = new ItemStack(Items.SPRUCE_LOG);
        stack.setTag(itemNbt);

        stack.setDisplayName(new StringTextComponent(ChatFormatting.DARK_PURPLE + "ENVIRONMENT " + ChatFormatting.GRAY + " - " + ChatFormatting.WHITE + " Environment Loader"));

        mc.playerController.sendPacketDropItem(stack);

        mc.player.sendMessage(new StringTextComponent(ChatFormatting.GOLD + " - " + ChatFormatting.YELLOW + "You have been given an environmentblock sender."));
        return 1;
    }
}
