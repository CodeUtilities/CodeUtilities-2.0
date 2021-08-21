package io.github.codeutilities.sys.aweslib;

import io.github.codeutilities.mod.config.Config;
import io.github.codeutilities.sys.file.FileUtil;
import io.github.codeutilities.sys.player.chat.ChatUtil;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.nio.file.Path;

public class SoundDownloader {

    // When a user consents to a sound (OR it was in the consent list) This is triggered.
    public static void consented(String sound) {

        AWEManager.maxMb = Config.getInteger("maxMB");
        AWEManager.maxAmnt = Config.getInteger("maxAmnt");

        // getDirectory
        Path dir = FabricLoader.getInstance().getGameDir().resolve("CodeUtilities/aweslib");
        // Check if the plot reaches the maxMB limit. It is possible for plots to kind of bypass this.
        if(FileUtil.bytesToMeg(FileUtil.getFolderSize(dir.toFile())) > AWEManager.maxMb) {
            ChatUtil.sendMessage(String.format("< aweslib > The plot %s reaches the maxMb limit of %f", AWEManager.plotName, AWEManager.maxMb));
        }
        // Check if the files in the folder is less than maxAmnt
        File[] fileAmount = dir.toFile().listFiles();
        if(fileAmount.length > AWEManager.maxAmnt) {
            ChatUtil.sendMessage(String.format("< aweslib > The plot %s reaches the maxAmnt limit of %f", AWEManager.plotName, AWEManager.maxMb));
        }
        // Does both checks.
        if(FileUtil.bytesToMeg(FileUtil.getFolderSize(dir.toFile())) < AWEManager.maxMb && fileAmount.length < AWEManager.maxAmnt) {
            // Makes sure the name for the file is legal.
            String name = AWEUtils.legalizeUrl(sound);
            String loc = dir.toString() + "/" + name; // Setting location.
            // Download fle:
            File sFile = FileUtil.download(sound, loc);
        }
        AWEManager.downloadedIndex++;
        int needed = AWEManager.sounds.size();;
        if(AWEManager.downloadedIndex == needed) {
            ChatUtil.sendMessage(String.format("< aweslib > All sounds of the plot %s have been downloaded!", AWEManager.plotName));
            AWEManager.downloadPhase = false;
        }
    }

}
