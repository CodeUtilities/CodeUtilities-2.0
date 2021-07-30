package io.github.codeutilities.sys.renderer.widgets;

import io.github.codeutilities.sys.util.ItemUtil;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

public class ClickableGiveItem extends CItem {

    public ClickableGiveItem(ItemStack stack) {
        super(stack);
    }

    @Override
    public InputResult onClick(int x, int y, int button) {
        MinecraftClient mc = MinecraftClient.getInstance();
        ItemUtil.giveCreativeItem(getItems().get(0), true);
        mc.player.playSound(SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 2, 1);
        return super.onClick(x,y,button);
    }
}
