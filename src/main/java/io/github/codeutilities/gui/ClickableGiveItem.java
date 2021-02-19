package io.github.codeutilities.gui;

import io.github.codeutilities.util.ItemUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.*;

public class ClickableGiveItem extends CItem {

    public ClickableGiveItem(ItemStack stack) {
        super(stack);
    }

    @Override
    public void onClick(int x, int y, int button) {
        MinecraftClient mc = MinecraftClient.getInstance();
        ItemUtil.giveCreativeItem(getItems().get(0), true);
        mc.player.playSound(SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 2, 1);
    }
}
