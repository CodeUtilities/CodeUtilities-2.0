package io.github.codeutilities.sys.aweslib;

import com.google.gson.JsonObject;
import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.mod.config.Config;
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

public class AweManager implements ILoader {
    /*
    I documented what pretty much each stuff here does mainly for the developers
    and the people paranoid enough to read CodeUtilities source code.
    */

    // Constants basically or smth
    public static String AWEDATABASE = Config.getString("awesdb");

    // Plot related.
    public static String Token = "empty"; // This is for verifying the plot is not impersonating by the way.
    public static ArrayList<String> sounds = new ArrayList<String>();
    public static String pubSound; // Download Related Variable
    public static int DownloadedIndex;
    public static String plotID;

    // Booleans
    public static boolean PlotIsSet;
    public static boolean TokenIsSet;
    public static boolean stateisChanged = false;
    public static boolean Downloaded;
    public static boolean DownloadPhase;

    // Settings
    public static ArrayList<String> consented = new ArrayList<String>();
    public static int maxAmnt;
    public static int maxMb;

    // Executes then CodeUtils is launcher or smth idk dude
    public AweManager() {
        // bro idk
    }

    // Executes when plot is changed.
    public static void plotChange() {
        // Reset Plot vars
        Token = "empty";

        // Reset Booleans
        PlotIsSet = false;
        TokenIsSet = false;
        Downloaded = false;
        stateisChanged = false;

        // Clear aweslib sound cache
        CodeUtilities.log("[AWESLIB] Purging sound cache.");
        AweFile.cleanFolder("aweslib");
        CodeUtilities.log("[AWESLIB] Plot has been changed.");
    }

    // Executes whenever Plot, Token or sounds are set.
    public static void stateChange() {
        if (TokenIsSet && !stateisChanged) {
            stateisChanged = true;
            // Creating thread so game is not paused.
            Thread doStuff = new Thread(() -> {
                //String sendJson = "{ \"name\": \"Baeldung\", \"java\": true }";
                //JsonObject jsonObject = new JsonParser().parse(sendJson).getAsJsonObject();

                //WebUtil.makePost(AWEDATABASE + "/cuvalidate", jsonObject);
                State state = DFInfo.currentState;
                State.Plot plot = state.getPlot();
                plotID = plot.getId();

                try {
                    CodeUtilities.log("[AWESLIB] Asking our servers.");
                    // Ay guys lets ask aweslib backend if the plot we say we are is who we are.
                    JsonObject response = (JsonObject) WebUtil.getJson(AWEDATABASE + "cuvalidate/" + Token + "/" + plotID);
                    // Doing medbay scan to check if they're impasta.
                    if(response.get("state").getAsBoolean()) {
                        // WAIT? NO IMPASTA? LES GOOO (Also we wait 500 millis to make sure All sounds are in the ArrayList)
                        Thread.sleep(500);
                        if(!Downloaded) {
                            Downloaded = true;
                            DownloadPhase = true;
                            DownloadedIndex = 0;
                            ChatUtil.sendMessage("< aweslib > Downloading the sounds of the plot "+ plotID + " now. (This may take a minute or two)");

                            int i = 0;
                            for (String sound : sounds) {
                                i++;
                                pubSound = sound;
                                try {
                                    URI soundUrl = new URI(sound);
                                    if(consented.contains(soundUrl.getHost())) {
                                        SoundDownloader.consented(sound);
                                    } else {
                                        ChatUtil.sendMessage("< aweslib > Do you want to allow the plot to use " + soundUrl.getHost() + "\nType: \n/aweslib allow | /aweslib block | /aweslib report");
                                    }
                                } catch (URISyntaxException e) {
                                    e.printStackTrace();
                                }

                            }

                        }
                    }

                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }


            });
            doStuff.start();
        }
    }

    public void parseConsent() {

        String toParse = Config.getString("consented");

        String[] Splitted = toParse.split(",");

        ArrayList<String> toConsented = new ArrayList<String>(Arrays.asList(Splitted));
        consented = toConsented;
    }

    @Override
    public void load() {
        AWEDATABASE = Config.getString("awesdb");
        parseConsent();
        AweFile.createFolder("aweslib");

        maxMb = Config.getInteger("maxMB");
        maxAmnt = Config.getInteger("maxAmnt");
        CodeUtilities.log(String.valueOf(maxMb));
        CodeUtilities.log(String.valueOf(maxAmnt));
        CodeUtilities.log("[AWESLIB] Initialized!");
    }
}
