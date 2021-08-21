package io.github.codeutilities.sys.aweslib;

import com.google.gson.JsonObject;
import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.mod.config.Config;
import io.github.codeutilities.sys.file.FileUtil;
import io.github.codeutilities.sys.file.ILoader;
import io.github.codeutilities.sys.networking.State;
import io.github.codeutilities.sys.networking.WebUtil;
import io.github.codeutilities.sys.player.DFInfo;
import io.github.codeutilities.sys.player.chat.ChatUtil;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;

public class AWEManager implements ILoader {
    /*
    I documented what pretty much each stuff here does mainly for the developers
    and the people paranoid enough to read CodeUtilities source code.
    */

    // Constants basically or smth
    public static String AWEDATABASE = Config.getString("awesdb");

    // Plot related.
    public static String token = "empty"; // This is for verifying the plot is not impersonating by the way.
    public static ArrayList<String> sounds = new ArrayList<>();
    public static String pubSound; // Download Related Variable
    public static int downloadedIndex;
    public static String plotID;
    public static String plotName;

    // Booleans
    public static boolean plotIsSet;
    public static boolean tokenIsSet;
    public static boolean stateIsChanged = false;
    public static boolean downloaded;
    public static boolean downloadPhase;

    // Settings
    public static ArrayList<String> consented = new ArrayList<>();
    public static int maxAmnt;
    public static int maxMb;

    // Executes when plot is changed.
    public static void plotChange() {
        // Reset Plot vars
        token = "empty";
        sounds = new ArrayList<>();
        // Reset Booleans
        plotIsSet = false;
        tokenIsSet = false;
        downloaded = false;
        stateIsChanged = false;

        // Clear aweslib sound cache
        CodeUtilities.log("[AWESLIB] Purging sound cache.");
        FileUtil.cleanFolder("aweslib");
        CodeUtilities.log("[AWESLIB] Plot has been changed.");
    }

    // Executes whenever Plot, Token or sounds are set.
    public static void stateChange() {
        if (tokenIsSet && !stateIsChanged) {
            stateIsChanged = true;
            // Creating thread so game is not paused.
            new Thread(() -> {
                State state = DFInfo.currentState;
                State.Plot plot = state.getPlot();
                plotID = plot.getId();
                plotName = plot.getName();

                try {
                    parseConsent();
                    AWEDATABASE = Config.getString("awesdb");
                    CodeUtilities.log("[AWESLIB] Asking our servers.");
                    // Ay guys lets ask aweslib backend if the plot we say we are is who we are.
                    JsonObject response = (JsonObject) WebUtil.getJson(AWEDATABASE + "cuvalidate/" + token + "/" + plotID);
                    // Doing medbay scan to check if they're impasta.
                    if(response.get("state").getAsBoolean()) {
                        // WAIT? NO IMPASTA? LES GOOO (Also we wait 500 millis to make sure All sounds are in the ArrayList)
                        Thread.sleep(500);
                        if(!downloaded) {
                            downloaded = true;
                            downloadPhase = true;
                            downloadedIndex = 0;
                            ChatUtil.sendMessage(String.format("< aweslib > Downloading the sounds of the plot %s now. (This may take a minute or two)", plotName));

                            int i = 0;
                            for (String sound : sounds) {
                                i++;
                                pubSound = sound;
                                try {
                                    URI soundUrl = new URI(sound);
                                    if(consented.contains(soundUrl.getHost())) {
                                        SoundDownloader.consented(sound);
                                    } else {
                                        ChatUtil.sendMessage(String.format("< aweslib > Do you want to allow the plot to use %s\nType: \n/aweslib allow | /aweslib block | /aweslib report", soundUrl.getHost()));
                                    }
                                } catch (URISyntaxException e) {
                                    AWEUtils.sendMessage("Something weird has happened whilst downloading sounds.");
                                }
                                int e = 0;
                                while (i == downloadedIndex) {
                                    e++;
                                }

                            }

                        }
                    }

                } catch (IOException | InterruptedException e) {
                    AWEUtils.sendMessage("Something weird has happened whilst downloading sounds.");
                }


            }).start();
        }
    }

    public static void parseConsent() {

        String toParse = Config.getString("consented");

        String[] splitted = toParse.split(",");

        ArrayList<String> toConsented = new ArrayList<String>(Arrays.asList(splitted));
        consented = toConsented;
    }

    @Override
    public void load() {
        AWEDATABASE = Config.getString("awesdb");
        parseConsent();
        FileUtil.createFolder("aweslib");

        maxMb = Config.getInteger("maxMB");
        maxAmnt = Config.getInteger("maxAmnt");
        CodeUtilities.log("[AWESLIB] Initialized!");
    }
}