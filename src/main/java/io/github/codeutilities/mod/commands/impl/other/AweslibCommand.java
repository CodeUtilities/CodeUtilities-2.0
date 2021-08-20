package io.github.codeutilities.mod.commands.impl.other;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.github.codeutilities.mod.commands.Command;
import io.github.codeutilities.mod.commands.arguments.ArgBuilder;
import io.github.codeutilities.sys.aweslib.AweManager;
import io.github.codeutilities.sys.aweslib.AweUtils;
import io.github.codeutilities.sys.aweslib.SoundDownloader;
import io.github.codeutilities.sys.player.chat.ChatUtil;

import java.util.Objects;

import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;

public class AweslibCommand extends Command {

    @Override
    public void register(MinecraftClient mc, CommandDispatcher<FabricClientCommandSource> cd) {
        cd.register(ArgBuilder.literal("aweslib")
                .then(ArgBuilder.literal("allow")).executes(ctx -> {

                    if(AweManager.DownloadPhase) {
                        AweUtils.sendMessage("You have decided to allow this domain.");
                        AweUtils.sendMessage("If you want this domain to always be allowed go to the settings.");
                        new Thread(() -> {
                            SoundDownloader.consented(AweManager.pubSound);
                        }).start();
                    }

                    return 1;
                })
                .then(ArgBuilder.literal("block")).executes(ctx -> {

                    if(AweManager.DownloadPhase) {
                        AweUtils.sendMessage("You have decided to block this domain. (not saved)");
                        AweManager.DownloadedIndex++;
                        if(AweManager.sounds.size() == AweManager.DownloadedIndex) {
                            AweUtils.sendMessage("All sounds of the plot " + AweManager.plotID + " have been downloaded!");
                            AweManager.DownloadPhase = false;
                        }
                    }

                    return 1;
                })
                .then(ArgBuilder.literal("report")).executes(ctx -> {

                    if(AweManager.DownloadPhase) {
                        AweUtils.sendMessage("Domain Blocked, to report this Plot send a message to SirObby_ or (Sir Obsidian#2640).");
                        AweManager.DownloadedIndex++;
                        if(AweManager.sounds.size() == AweManager.DownloadedIndex) {
                            AweUtils.sendMessage("All sounds of the plot " + AweManager.plotID + " have been downloaded!");
                            AweManager.DownloadPhase = false;
                        }
                    }

                    return 1;
                })
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
