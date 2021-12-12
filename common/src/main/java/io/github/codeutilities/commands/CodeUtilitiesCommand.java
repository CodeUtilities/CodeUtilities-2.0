package io.github.codeutilities.commands;

import com.mojang.brigadier.CommandDispatcher;
import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.commands.sys.Command;
import io.github.codeutilities.menus.CodeUtilitiesMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.SharedSuggestionProvider;

public class CodeUtilitiesCommand implements Command {

    @Override
    public void register(CommandDispatcher<SharedSuggestionProvider> dispatcher) {
        dispatcher.register(CommandHandler.literal("codeutilities")
            .executes(ctx -> {
                Minecraft mc = CodeUtilities.MC;

                mc.tell(() -> mc.setScreen(new CodeUtilitiesMenu()));
                return CommandHandler.CANCEL_MESSAGE;
            })
        );
    }

    @Override
    public String getName() {
        return "/codeutilities";
    }

    @Override
    public String getDescription() {
        return "Command for opening the Codeutilities menu";
    }
}
