package io.github.codeutilities.mod.commands.impl.text;

import static io.github.codeutilities.mod.commands.arguments.ArgBuilder.argument;
import static io.github.codeutilities.mod.commands.arguments.ArgBuilder.literal;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import io.github.codeutilities.mod.commands.Command;
import io.github.codeutilities.sys.util.TextUtil;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class ActionbarCommand extends Command {

    @Override
    public void register(MinecraftClient mc, CommandDispatcher<FabricClientCommandSource> cd) {
        reg("previewactionbar",mc,cd);
        reg("actionbarpreview",mc,cd);
    }

    @Override
    public String getDescription() {
        return "[blue]/previewactionbar [text][reset]\n"
            + "[blue]/actionbarpreview [text][reset]\n"
            + "\n"
            + "Previews the action bar text. If no text is specified, the name of the item you are holding will show up.";
    }

    @Override
    public String getName() {
        return "/previewactionbar";
    }

    public void reg(String name, MinecraftClient mc, CommandDispatcher<FabricClientCommandSource> cd) {
        cd.register(literal(name)
            .then(argument("message", StringArgumentType.greedyString())
                .executes(ctx -> {
                    Text msg = TextUtil.colorCodesToTextComponent(
                        ctx.getArgument("message", String.class)
                            .replace("&", "§"));

                    mc.player.sendMessage(msg, true);
                    return 1;
                })
            )
            .executes(ctx -> {
                mc.player.sendMessage(mc.player.getMainHandStack().getName(),true);
                return 1;
            })
        );
    }
}
