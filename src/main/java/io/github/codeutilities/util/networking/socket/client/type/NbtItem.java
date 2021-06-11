//TODO: Update to 1.17

//package io.github.codeutilities.util.networking.socket.client.type;
//
//import com.mojang.brigadier.StringReader;
//import com.mojang.brigadier.exceptions.CommandSyntaxException;
//import net.minecraft.item.ItemStack;
//import net.minecraft.nbt.StringNbtReader;
//
//import java.io.IOException;
//
//public class NbtItem extends SocketItem {
//
//    @Override
//    public String getIdentifier() {
//        return "nbt";
//    }
//
//    @Override
//    public ItemStack getItem(String data) throws Exception {
//        ItemStack stack;
//        try {
//            stack = ItemStack.fromNbt(new StringNbtReader(new StringReader(data)).parseCompound());
//        } catch (RuntimeException | CommandSyntaxException e) {
//            throw new IOException("Failed to parse provided NBT data.");
//        }
//
//        return stack;
//    }
//}
