package io.github.codeutilities.sys.util.misc;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.codeutilities.CodeUtilities;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;

public class ItemUtil {

    public static void giveCreativeItem(ItemStack item, boolean preferHand) {
        MinecraftClient mc = CodeUtilities.MC;
        DefaultedList<ItemStack> mainInventory = mc.player.getInventory().main;

        if (preferHand) {
            if (mc.player.getMainHandStack().isEmpty()) {
                mc.interactionManager.clickCreativeStack(item, mc.player.getInventory().selectedSlot + 36);
                return;
            }
        }

        for (int index = 0; index < mainInventory.size(); index++) {
            ItemStack i = mainInventory.get(index);
            ItemStack compareItem = i.copy();
            compareItem.setCount(item.getCount());
            if (item == compareItem) {
                while (i.getCount() < i.getMaxCount() && item.getCount() > 0) {
                    i.setCount(i.getCount() + 1);
                    item.setCount(item.getCount() - 1);
                }
            } else {
                if (i.getItem() == Items.AIR) {
                    if (index < 9)
                        MinecraftClient.getInstance().interactionManager.clickCreativeStack(item, index + 36);
                    mainInventory.set(index, item);
                    return;
                }
            }
        }
    }

    /**
     * Sets the item at a container slot. (Only works in creative)
     *
     * @param slot      The slot you want to change.
     * @param itemStack The item stack to replace it with
     */
    public static void setContainerItem(int slot, ItemStack itemStack) {
        MinecraftClient mc = CodeUtilities.MC;

        // this method kinda doesnt work in survival mode so let's throw an exception if this happens.
        if (!mc.player.isCreative()) {
            throw new IllegalStateException("Player is not in creative mode.");
        }

        // replace the 8th slot with the item we want to set.
        ItemStack replacedItem = mc.player.getInventory().main.get(7);
        CodeUtilities.MC.interactionManager.clickCreativeStack(itemStack, 43);
        mc.player.getInventory().main.set(7, itemStack);

        // simulates pressing the 8 key on the slot we want to change.
        CodeUtilities.MC.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, slot, 7, SlotActionType.SWAP, CodeUtilities.MC.player);

        // change the 8th slot back to what it was before.
        CodeUtilities.MC.interactionManager.clickCreativeStack(replacedItem, 43);
        mc.player.getInventory().main.set(7, replacedItem);
    }

    public static List<ItemStack> fromItemContainer(ItemStack container) {
        NbtList nbt = container.getOrCreateNbt().getCompound("BlockEntityTag").getList("Items", 10);
        return fromListTag(nbt);
    }

    public static ItemStack fromID(String id) {
        return new ItemStack(Registry.ITEM.get(new Identifier(id.toLowerCase())));
    }

    public static void setLore(ItemStack itemStack, Text[] lores){
        NbtList loreTag = new NbtList();
        for(Text lore : lores) {
            if(lore == null){
                itemStack.getSubNbt("display").put("Lore", loreTag);
                return;
            }
            loreTag.add(NbtString.of("{\"extra\":[{\"bold\":" + lore.getStyle().isBold() + ",\"italic\":" + lore.getStyle().isItalic() + ",\"underlined\":" + lore.getStyle().isUnderlined() + ",\"strikethrough\":" + lore.getStyle().isStrikethrough() + ",\"obfuscated\":" + lore.getStyle().isObfuscated() + ",\"color\":\"" + lore.getStyle().getColor() + "\",\"text\":\"" + lore.getString() + "\"}],\"text\":\"\"}"));
        }
        itemStack.getSubNbt("display").put("Lore", loreTag);
    }

    public static ItemStack setLore(ItemStack itemStack, String[] lores){
        NbtList loreTag = new NbtList();
        for(String lore : lores) {
            if(lore == null){
                itemStack.getSubNbt("display").put("Lore", loreTag);
                return itemStack;
            }
            loreTag.add(NbtString.of(lore));
        }
        itemStack.getSubNbt("display").put("Lore", loreTag);
        return itemStack;
    }

    public static ItemStack addLore(ItemStack itemStack, String[] lores){
        NbtList loreTag = new NbtList();
        if(itemStack.getOrCreateSubNbt("display").contains("Lore")){
            loreTag = itemStack.getSubNbt("display").getList("Lore", 8);
        }
        for(String lore : lores) {
            if(lore == null){
                break;
            }
            loreTag.add(NbtString.of(lore));
        }
        itemStack.getSubNbt("display").put("Lore", loreTag);
        return itemStack;
    }

    public static void givePlayerHead(String texture) throws CommandSyntaxException {
        ItemStack item = new ItemStack(Items.PLAYER_HEAD);

        if (texture.contains(".minecraft.net")) {
            Charset charset = StandardCharsets.UTF_8;
            String rawString = "{\"textures\":{\"SKIN\":{\"url\":\"" + texture + "\"}}}";

            byte[] a = Base64.getEncoder().encode(rawString.getBytes(charset));
            texture = new String(a, charset);
        }

        NbtCompound nbt = StringNbtReader.parse("{SkullOwner:{Id:" + StringUtil.genDummyIntArray() + ",Properties:{textures:[{Value:\"" + texture + "\"}]}}}");
        item.setNbt(nbt);
        ItemUtil.giveCreativeItem(item, true);
    }

    public static boolean isVar(ItemStack stack, String type) {
        try {
            NbtCompound tag = stack.getNbt();
            if (tag == null) {
                return false;
            }

            NbtCompound publicBukkitNBT = tag.getCompound("PublicBukkitValues");
            if (publicBukkitNBT == null) {
                return false;
            }

            if (publicBukkitNBT.getString("hypercube:varitem").length() > 0) {
                return CodeUtilities.JSON_PARSER.parse(publicBukkitNBT.getString("hypercube:varitem")).getAsJsonObject().get("id").getAsString().equalsIgnoreCase(type);
            }

            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static NbtList toListTag(List<ItemStack> stacks) {
        NbtList listTag = new NbtList();
        for (ItemStack stack : stacks) {
            listTag.add(stack.writeNbt(new NbtCompound()));
        }

        return listTag;
    }

    public static List<ItemStack> fromListTag(NbtList listTag) {
        List<ItemStack> stacks = new ArrayList<>();
        for (NbtElement tag : listTag) {
            stacks.add(ItemStack.fromNbt((NbtCompound) tag));
        }
        return stacks;
    }
}
