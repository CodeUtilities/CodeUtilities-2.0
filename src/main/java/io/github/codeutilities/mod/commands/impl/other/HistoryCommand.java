package io.github.codeutilities.mod.commands.impl.other;


import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.codeutilities.mod.PlotHistoryRecorder;
import io.github.codeutilities.mod.commands.Command;
import io.github.codeutilities.mod.commands.arguments.ArgBuilder;
import io.github.codeutilities.mod.config.Config;
import io.github.codeutilities.sys.SimplePlot;
import io.github.codeutilities.sys.player.chat.ChatType;
import io.github.codeutilities.sys.player.chat.ChatUtil;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.*;

import java.util.ArrayList;

public class HistoryCommand extends Command {

    @Override
    public void register(MinecraftClient mc, CommandDispatcher<FabricClientCommandSource> cd) {
        LiteralArgumentBuilder<FabricClientCommandSource> cmd = ArgBuilder.literal("history")
                .executes(ctx -> {
                    if(!Config.getBoolean("plotHistory")) {
                        ChatUtil.sendMessage(new LiteralText("Plot history is not enabled in the settings."), ChatType.FAIL);
                        return 1;
                    }

                    ArrayList<SimplePlot> plots = PlotHistoryRecorder.getHistory();
                    if(plots.size() == 0) {
                        ChatUtil.sendMessage(new LiteralText("no plots in the history."), ChatType.FAIL);
                        return 1;
                    }

                    ChatUtil.sendMessage("");
                    ChatUtil.sendMessage(
                                    new LiteralText("⏪  ")
                                            .styled(style -> style.withColor(TextColor.fromRgb(0x1f9947))).append(
                                                    new LiteralText("            CodeUtilities Plot History  ")
                                                            .styled(style -> style.withColor(TextColor.fromRgb(0x33ffa7))).append(
                                                                    new LiteralText("⏩")
                                                                            .styled(style -> style.withColor(TextColor.fromRgb(0x1f9947)))
                                                            )), null);

                    for (SimplePlot entry : plots) {
                        MutableText entrymsg = new LiteralText(entry.getName())
                                .styled(style -> style.withColor(TextColor.fromRgb(0x00bbff)
                                                ).withClickEvent(
                                                        new ClickEvent(
                                                                ClickEvent.Action.RUN_COMMAND,
                                                                "/join " + entry.getId()
                                                        ))
                                                .withHoverEvent(
                                                        new HoverEvent(
                                                                HoverEvent.Action.SHOW_TEXT,
                                                                new LiteralText("Click to join!").styled(style2 -> style2.withColor(TextColor.fromRgb(0xaaaaaa)))
                                                        )
                                                )
                                ).append(
                                        new LiteralText(" - ").styled(style -> style.withColor(TextColor.fromRgb(0x555555))).append(
                                                new LiteralText(entry.getId()).styled(style -> style.withColor(TextColor.fromRgb(0xaaaaaa)))
                                                        .append(
                                                                new LiteralText(" by " + entry.getOwner()).styled(style -> style.withColor(TextColor.fromRgb(0x555555))))));
                        ChatUtil.sendMessage(entrymsg, null);
                    }

                    return 1;
                });
        cd.register(cmd);
    }

    @Override
    public String getDescription() {
        return "[blue]/history[reset]\n"
                + "\n"
                + "lists your recently joined plots.";
    }

    @Override
    public String getName() {
        return "/history";
    }
}