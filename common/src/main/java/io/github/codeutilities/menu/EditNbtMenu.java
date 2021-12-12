package io.github.codeutilities.menu;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.menu.widget.CPanel;
import io.github.codeutilities.menu.widget.CTextField;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.nbt.TextComponentTagVisitor;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;

public class EditNbtMenu extends Screen {

    final CPanel panel;
    final ItemStack item;

    public EditNbtMenu(ItemStack item) {
        super(new TextComponent("EditNbt"));
        panel = new CPanel(120, 100);
        this.item = item;

        String nbt = "{}";

        try {
            CompoundTag tag = item.getTag();
            if (tag != null) {
                nbt = new TextComponentTagVisitor("  ", 0).visit(tag).getString();
            }
        } catch (Exception ignored) {}

        CTextField textField = new CTextField(nbt, 2, 2,116, 96, true);

        textField.setChangedListener(() -> {
            try {
                CompoundTag tag = TagParser.parseTag(textField.getText());
                item.setTag(tag);
                textField.textColor = 0xFFFFFFFF;
            } catch (Exception err) {
                textField.textColor = 0xFFFF3333;
            }
        });

        panel.add(textField);
    }

    @Override
    public void onClose() {
        CodeUtilities.MC.gameMode.handleCreativeModeItemAdd(item, CodeUtilities.MC.player.getInventory().selected+36);
        super.onClose();
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float tickDelta) {
        renderBackground(stack);
        panel.render(stack, mouseX, mouseY, tickDelta);
        super.render(stack, mouseX, mouseY, tickDelta);
    }

    @Override
    public boolean charTyped(char c, int i) {
        panel.charTyped(c, i);
        return super.charTyped(c, i);
    }

    @Override
    public boolean keyPressed(int i, int j, int k) {
        panel.keyPressed(i, j, k);
        return super.keyPressed(i, j, k);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        panel.mouseClicked(mouseX, mouseY, button);
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double d, double e, double f) {
        panel.mouseScrolled(d, e, f);
        return super.mouseScrolled(d, e, f);
    }
}
