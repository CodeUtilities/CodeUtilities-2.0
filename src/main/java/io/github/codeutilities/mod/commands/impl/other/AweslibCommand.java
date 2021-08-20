package io.github.codeutilities.mod.commands.impl.other;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.github.codeutilities.mod.commands.Command;
import io.github.codeutilities.mod.commands.arguments.ArgBuilder;
import io.github.codeutilities.sys.aweslib.awemanager;
import io.github.codeutilities.sys.aweslib.SoundDownloader;
import io.github.codeutilities.sys.player.chat.ChatType;
import io.github.codeutilities.sys.player.chat.ChatUtil;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;

public class AweslibCommand extends Command {

    private static int exec(CommandContext<FabricClientCommandSource> ctx) {
        String action = ctx.getArgument("action", String.class);

        if(!awemanager.DownloadPhase) {
            ChatUtil.sendMessage("< aweslib > A /aweslib action is not required now.");
            return 1;
        }

        if(Objects.equals(action, "allow")) {
            ChatUtil.sendMessage("< aweslib > You have decided to allow this domain.");
            ChatUtil.sendMessage("< aweslib > If you want this domain to always be allowed go to the settings.");
            Thread downloadSnd = new Thread(() -> {
                SoundDownloader.consented(awemanager.pubSound);
            });
            downloadSnd.start();
        }

        if(Objects.equals(action, "block")) {
            ChatUtil.sendMessage("< aweslib > You have decided to block this domain. (not saved)");
            awemanager.DownloadedIndex++;
            if(awemanager.sounds.size() == awemanager.DownloadedIndex) {
                ChatUtil.sendMessage("< aweslib > All sounds of the plot " + awemanager.plotID + " have been downloaded!");
                awemanager.DownloadPhase = false;
            }
        }

        if(Objects.equals(action, "report")) {
            ChatUtil.sendMessage("< aweslib > Domain Blocked, to report this Plot send a message to SirObby_ or (Sir Obsidian#2640).");
            awemanager.DownloadedIndex++;
            if(awemanager.sounds.size() == awemanager.DownloadedIndex) {
                ChatUtil.sendMessage("< aweslib > All sounds of the plot " + awemanager.plotID + " have been downloaded!");
                awemanager.DownloadPhase = false;
            }
        }

        return 1;
    }

    @Override
    public void register(MinecraftClient mc, CommandDispatcher<FabricClientCommandSource> cd) {
        cd.register(ArgBuilder.literal("aweslib")
                .then(ArgBuilder.argument("action", StringArgumentType.greedyString())
                        .executes(AweslibCommand::exec)
                )
        );
    }

    @Override
    public String getDescription() {
        return "/aweslib <action>\n\nIt's aweslib command no explanations needed.";
    }

    @Override
    public String getName() {
        return "/aweslib";
    }
}
