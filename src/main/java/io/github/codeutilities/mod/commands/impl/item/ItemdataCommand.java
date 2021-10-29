package io.github.codeutilities.mod.commands.impl.item;

import com.mojang.brigadier.CommandDispatcher;
import io.github.codeutilities.mod.commands.Command;
import io.github.codeutilities.mod.commands.arguments.ArgBuilder;
import io.github.codeutilities.sys.player.chat.ChatType;
import io.github.codeutilities.sys.player.chat.ChatUtil;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.visitor.NbtTextFormatter;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.ClickEvent.Action;
import net.minecraft.text.LiteralText;
import net.minecraft.util.registry.Registry;

public class ItemdataCommand extends Command {

    @Override
    public void register(MinecraftClient mc, CommandDispatcher<FabricClientCommandSource> cd) {
        cd.register(ArgBuilder.literal("itemdata")
                .executes(ctx -> {
                    ItemStack item = mc.player.getMainHandStack();
                    NbtCompound nbt = item.getNbt();
                    if (nbt != null) {
                        ChatUtil.sendMessage(String.format("§5----------§dItem Data for %s§5----------", item.getName().getString()));
                        NbtTextFormatter formatter = new NbtTextFormatter("  ",0);

                        mc.player.sendMessage(formatter.apply(nbt), false);

                        String formatted = formatter.apply(nbt).getString();
                        String unformatted = nbt.toString();

                        LiteralText msg1 = new LiteralText("§5Click here to copy a ");
                        LiteralText msg2 = new LiteralText("§d§lFormatted§5, ");
                        LiteralText msg3 = new LiteralText("§d§lUnformatted");
                        LiteralText msg4 = new LiteralText("§5 or ");
                        LiteralText msg5 = new LiteralText("§d§l/dfgive");
                        LiteralText msg6 = new LiteralText("§5 version!");

                        msg2.styled((style) -> style.withClickEvent(new ClickEvent(Action.RUN_COMMAND, "/copytxt " + formatted)));
                        msg3.styled((style) -> style.withClickEvent(new ClickEvent(Action.RUN_COMMAND, "/copytxt " + unformatted)));
                        msg5.styled((style) -> style.withClickEvent(new ClickEvent(Action.RUN_COMMAND, "/copytxt " + "/dfgive " + Registry.ITEM.getId(item.getItem()) + unformatted + " " + item.getCount())));

                        this.sendMessage(mc, msg1.append(msg2).append(msg3).append(msg4).append(msg5).append(msg6));

                    } else {
                        ChatUtil.sendMessage("No NBT data found!", ChatType.FAIL);
                    }
                    return 1;
                })
        );
    }

    @Override
    public String getDescription() {
        return """
            [blue]/itemdata[reset]

            Shows the item NBT of the item you are holding.""";
    }

    @Override
    public String getName() {
        return "/itemdata";
    }
}
