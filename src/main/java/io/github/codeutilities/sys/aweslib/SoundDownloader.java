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
        if(AweFile.bytesToMeg(AweFile.getFolderSize(dir.toFile())) > AweManager.maxMb) {
            ChatUtil.sendMessage("< aweslib > The plot "+ AweManager.plotID + " reaches the maxMb limit of " + AweManager.maxMb);
        }
        // Check if the files in the folder is less htan maxAmnt
        java.io.File[] fileAmount = dir.toFile().listFiles();
        if(fileAmount.length > AweManager.maxAmnt) {
            ChatUtil.sendMessage("< aweslib > The plot "+ AweManager.plotID + " reaches the maxAmnt limit of " + AweManager.maxAmnt);
        }
        // Does both checks.
        if(AweFile.bytesToMeg(AweFile.getFolderSize(dir.toFile())) < AweManager.maxMb && fileAmount.length < AweManager.maxAmnt) {
            // Makes sure the name for the file is legal.
            String name = sound.replace("/", "").replace(":", "").replace(".", "") + ".wav";
            String loc = dir.toString() + "/" + name; // Setting location.
            // Download fle:
            File sFile = download(sound, loc);
        }
        AweManager.DownloadedIndex++;
        int needed = AweManager.sounds.size();;
        if(AweManager.DownloadedIndex == needed) {
            ChatUtil.sendMessage("< aweslib > All sounds of the plot " + AweManager.plotID + " have been downloaded!");
            AweManager.DownloadPhase = false;
        }
    }

}
