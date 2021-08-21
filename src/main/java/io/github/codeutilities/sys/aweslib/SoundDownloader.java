package io.github.codeutilities.sys.aweslib;

import io.github.codeutilities.sys.player.chat.ChatUtil;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;

public class SoundDownloader {

    // This is a SYSTEM function it is triggered by consented().
    public static File download(String URL, String ToLocation) {
        try {
            FileUtils.copyURLToFile(new URL(URL), new File(ToLocation), 10000, 10000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // When a user consents to a sound (OR it was in the consent list) This is triggered.
    public static void consented(String sound) {
        // getDirectory
        Path dir = FabricLoader.getInstance().getGameDir().resolve("CodeUtilities").resolve("aweslib");
        // Check if the plot reaches the maxMB limit. It is possible for plots to kind of bypass this.
        if(AWEFile.bytesToMeg(AWEFile.getFolderSize(dir.toFile())) > AWEManager.maxMb) {
            ChatUtil.sendMessage("< aweslib > The plot "+ AWEManager.plotID + " reaches the maxMb limit of " + AWEManager.maxMb);
        }
        // Check if the files in the folder is less htan maxAmnt
        File[] fileAmount = dir.toFile().listFiles();
        if(fileAmount.length > AWEManager.maxAmnt) {
            ChatUtil.sendMessage("< aweslib > The plot "+ AWEManager.plotID + " reaches the maxAmnt limit of " + AWEManager.maxAmnt);
        }
        // Does both checks.
        if(AWEFile.bytesToMeg(AWEFile.getFolderSize(dir.toFile())) < AWEManager.maxMb && fileAmount.length < AWEManager.maxAmnt) {
            // Makes sure the name for the file is legal.
            String name = sound.replace("/", "").replace(":", "").replace(".", "") + ".wav";
            String loc = dir.toString() + "/" + name; // Setting location.
            // Download fle:
            File sFile = download(sound, loc);
        }
        AWEManager.downloadedIndex++;
        int needed = AWEManager.sounds.size();;
        if(AWEManager.downloadedIndex == needed) {
            ChatUtil.sendMessage("< aweslib > All sounds of the plot " + AWEManager.plotID + " have been downloaded!");
            AWEManager.downloadPhase = false;
        }
    }

}
