package io.github.codeutilities.commands.impl;

import com.mojang.brigadier.CommandDispatcher;
import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.commands.Command;
import io.github.codeutilities.commands.CommandHandler;
import io.github.codeutilities.menus.EditNbtMenu;
import io.github.codeutilities.util.ChatUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.world.item.ItemStack;

public class EditNbtCommand implements Command {

    @Override
    public String getName() {
        return "/editnbt";
    }

    @Override
    public String getDescription() {
        return "Edit the nbt of an item.";
    }

    @Override
    public void register(CommandDispatcher<SharedSuggestionProvider> dispatcher) {
        dispatcher.register(CommandHandler.literal("editnbt")
            .executes(ctx -> {
                Minecraft mc = CodeUtilities.MC;

                ItemStack item = mc.player.getMainHandItem();

                if (item.isEmpty()) {
                    ChatUtil.displayClientMessage("§cYou must be holding an item to use this command.");
                    return 1;
                }

                if (!mc.player.isCreative()) {
                    ChatUtil.displayClientMessage("§cYou must be in creative mode to use this command.");
                    return 1;
                }

                mc.tell(() -> mc.setScreen(new EditNbtMenu(item)));

                return 1;
            })
        );
    }
}
