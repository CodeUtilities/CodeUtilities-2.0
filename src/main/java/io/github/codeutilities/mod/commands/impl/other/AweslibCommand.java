package io.github.codeutilities.mod.commands.impl.other;

import com.mojang.brigadier.CommandDispatcher;
import io.github.codeutilities.mod.commands.Command;
import io.github.codeutilities.mod.commands.arguments.ArgBuilder;
import io.github.codeutilities.sys.aweslib.AweManager;
import io.github.codeutilities.sys.aweslib.AweUtils;
import io.github.codeutilities.sys.aweslib.SoundDownloader;

import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;

public class AweslibCommand extends Command {

    @Override
    public void register(MinecraftClient mc, CommandDispatcher<FabricClientCommandSource> cd) {
        cd.register(ArgBuilder.literal("aweslib")
                .then(ArgBuilder.literal("allow")).executes(ctx -> {

                    if(AweManager.downloadPhase) {
                        AweUtils.sendMessage("You have decided to allow this domain.");
                        AweUtils.sendMessage("If you want this domain to always be allowed go to the settings.");
                        new Thread(() -> {
                            SoundDownloader.consented(AweManager.pubSound);
                        }).start();
                    }

                    return 1;
                })
                .then(ArgBuilder.literal("block")).executes(ctx -> {

                    if(AweManager.downloadPhase) {
                        AweUtils.sendMessage("You have decided to block this domain. (not saved)");
                        AweManager.downloadedIndex++;
                        if(AweManager.sounds.size() == AweManager.downloadedIndex) {
                            AweUtils.sendMessage("All sounds of the plot " + AweManager.plotID + " have been downloaded!");
                            AweManager.downloadPhase = false;
                        }
                    }

                    return 1;
                })
                .then(ArgBuilder.literal("report")).executes(ctx -> {

                    if(AweManager.downloadPhase) {
                        AweUtils.sendMessage("Domain Blocked, to report this Plot send a message to SirObby_ or (Sir Obsidian#2640).");
                        AweManager.downloadedIndex++;
                        if(AweManager.sounds.size() == AweManager.downloadedIndex) {
                            AweUtils.sendMessage("All sounds of the plot " + AweManager.plotID + " have been downloaded!");
                            AweManager.downloadPhase = false;
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
