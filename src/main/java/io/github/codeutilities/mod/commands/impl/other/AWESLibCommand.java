package io.github.codeutilities.mod.commands.impl.other;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.github.codeutilities.mod.commands.Command;
import io.github.codeutilities.mod.commands.arguments.ArgBuilder;
import io.github.codeutilities.mod.commands.arguments.types.FileArgumentType;
import io.github.codeutilities.sys.aweslib.AWEManager;
import io.github.codeutilities.sys.aweslib.AWEUtils;
import io.github.codeutilities.sys.aweslib.SoundDownloader;

import io.github.codeutilities.sys.file.ExternalFile;
import io.github.codeutilities.sys.hypercube.templates.TemplateUtils;
import io.github.codeutilities.sys.player.chat.ChatType;
import io.github.codeutilities.sys.player.chat.ChatUtil;
import io.github.codeutilities.sys.util.ItemUtil;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.LiteralText;

import java.io.File;

public class AWESLibCommand extends Command {

    private static int run2(CommandContext<FabricClientCommandSource> ctx) {
        if (AWEManager.downloadPhase) {
            AWEUtils.sendMessage("Domain Blocked, to report this Plot send a message to SirObby_ or (Sir Obsidian#2640).");
            AWEManager.downloadedIndex++;
            if (AWEManager.sounds.size() == AWEManager.downloadedIndex) {
                AWEUtils.sendMessage("All sounds of the plot " + AWEManager.plotID + " have been downloaded!");
                AWEManager.downloadPhase = false;
            }
        } else {
            AWEUtils.sendMessage("There is no need for an /aweslib action.");
        }
        return 1;
    }

    private static int run3(CommandContext<FabricClientCommandSource> ctx) {
        if (AWEManager.downloadPhase) {
            AWEUtils.sendMessage("You have decided to allow this domain.");
            AWEUtils.sendMessage("If you want this domain to always be allowed go to the settings.");
            new Thread(() -> {
                SoundDownloader.consented(AWEManager.pubSound);
            }).start();
        } else {
            AWEUtils.sendMessage("There is no need for an /aweslib action.");
        }
        return 1;
    }

    @Override
    public void register(MinecraftClient mc, CommandDispatcher<FabricClientCommandSource> cd) {
        cd.register(ArgBuilder.literal("aweslib")
                .then(ArgBuilder.literal("allow")
                        .executes(AWESLibCommand::run3)
                )
                .then(ArgBuilder.literal("block")
                        .executes(this::run)
                ).then(ArgBuilder.literal("report")
                        .executes(AWESLibCommand::run2)
                )
        );
    }

    @Override
    public String getDescription() {
        return "/aweslib allow\n/aweslib block\n/aweslib report\n\nIts for what action to do for a domain when downloading..";
    }

    @Override
    public String getName() {
        return "/aweslib";
    }

    private int run(CommandContext<FabricClientCommandSource> ctx) {
        if (AWEManager.downloadPhase) {
            AWEUtils.sendMessage("You have decided to block this domain. (not saved)");
            AWEManager.downloadedIndex++;
            if (AWEManager.sounds.size() == AWEManager.downloadedIndex) {
                AWEUtils.sendMessage("All sounds of the plot " + AWEManager.plotID + " have been downloaded!");
                AWEManager.downloadPhase = false;
            }
        } else {
            AWEUtils.sendMessage("There is no need for an /aweslib action.");
        }
        return 1;
    }
}
