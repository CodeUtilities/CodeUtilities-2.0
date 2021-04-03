package io.github.codeutilities.dfrpc;

import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.IPCListener;
import com.jagrosh.discordipc.entities.RichPresence;
import com.jagrosh.discordipc.exceptions.NoDiscordClientException;
import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.config.ModConfig;
import io.github.codeutilities.events.ChatReceivedEvent;
import io.github.codeutilities.util.DFInfo;
import net.minecraft.client.MinecraftClient;
import org.apache.logging.log4j.Level;

import java.time.OffsetDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DFDiscordRPC {

    public static boolean locating = false;
    public static boolean delayRPC = false;

    private static boolean firstLocate = true;
    private static boolean firstUpdate = true;
    private static String oldMode = "";
    private static OffsetDateTime time;

    private static IPCClient client;
    public static RichPresence.Builder builder;

    public static void main() throws Exception {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            CodeUtilities.log(Level.INFO, "Closing Discord hook.");
            client.close();
        }));

        client = new IPCClient(813925725718577202L);
        client.setListener(new IPCListener() {
            @Override
            public void onReady(IPCClient client) {
                RichPresence.Builder builder = new RichPresence.Builder();
                builder.setDetails("Idle")
                        .setStartTimestamp(OffsetDateTime.now())
                        .setLargeImage("canary-large", "Discord Canary")
                        .setSmallImage("ptb-small", "Discord PTB");
                io.github.codeutilities.dfrpc.DFDiscordRPC.builder = builder;
            }
        });

        DFRPCThread dfrpc = new DFRPCThread();
        dfrpc.start();

    }

    public static class DFRPCThread extends Thread {

        MinecraftClient mc = MinecraftClient.getInstance();

        public void run() {
            String oldState = "Not on DF";
            int i = 0;

            while (true) {

                if (DFInfo.isOnDF() && !delayRPC) {
                    if (!String.valueOf(DFInfo.currentState).equals(oldState)) {
                        locateRequest();
                    } else {
                        if (i % 30 == 0 && DFInfo.currentState == DFInfo.State.PLAY) locateRequest();
                    }
                } else {
                    firstLocate = true;
                    firstUpdate = true;
                    try {
                        client.close();
                    } catch (Exception ignored) {
                    }
                }

                if (!ModConfig.getConfig().discordRPC) {
                    firstLocate = true;
                    firstUpdate = true;
                    try {
                        client.close();
                    } catch (Exception ignored) {
                    }

                }

                if (DFInfo.isOnDF()) {
                    oldState = String.valueOf(DFInfo.currentState);
                } else {
                    oldState = "Not on DF";
                }

                if (delayRPC) {
                    delayRPC = false;
                    try {
                        DFRPCThread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    DFRPCThread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i++;

            }
        }

        public void locateRequest() {
            if (mc.player != null) {
                if (ModConfig.getConfig().discordRPC) {
                    mc.player.sendChatMessage("/locate");
                }
                locating = true;
                for (int i = 0; i < 15000; i++) {
                    try {
                        DFRPCThread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!locating) break;
                }
                locating = false;
            }

            if (firstLocate) {
                try {
                    client.connect();
                } catch (NoDiscordClientException ignored) {
                }
                updDiscord();
                firstLocate = false;
            } else {
                updDiscord();
                firstUpdate = false;
            }
            CodeUtilities.log(Level.INFO, "----------- RPC Updated! Status: " + client.getStatus());
        }
    }

    private static void updDiscord() {
        RichPresence.Builder presence = new RichPresence.Builder();
        String mode = "spawn";

        if (ChatReceivedEvent.dfrpcMsg.startsWith("                                       \n" +
                "You are currently at spawn.\n")) {
            presence.setDetails("At spawn");
            presence.setState(ChatReceivedEvent.dfrpcMsg.replaceFirst("^                                       \n" +
                    "You are currently at spawn.\n", "").replaceFirst("^» Server: ", "").replaceFirst("\n" +
                    "                                       $", ""));
            presence.setSmallImage(null, null);
            presence.setLargeImage("diamondfirelogo", "mcdiamondfire.com");
        } else {
            // PLOT ID
            Pattern pattern = Pattern.compile("\\[[0-9]+]\n");
            Matcher matcher = pattern.matcher(ChatReceivedEvent.dfrpcMsg);
            String id = "";
            while (matcher.find()) {
                id = matcher.group();
            }
            id = id.replaceAll("\\[|]|\n", "");

            // PLOT NODE
            pattern = Pattern.compile("Node ([0-9]|Beta)\n");
            matcher = pattern.matcher(ChatReceivedEvent.dfrpcMsg);
            String node = "";
            while (matcher.find()) {
                node = matcher.group();
            }

            // PLOT NAME
            pattern = Pattern.compile("\n\n» .+ \\[[0-9]+]\n");
            matcher = pattern.matcher(ChatReceivedEvent.dfrpcMsg);
            String name = "";
            while (matcher.find()) {
                name = matcher.group();
            }
            name = name.replaceAll("(^\n\n» )|( \\[[0-9]+]\n$)", "");

            // CUSTOM STATUS
            String customStatus = "";
            if (DFInfo.currentState == DFInfo.State.PLAY) {
                pattern = Pattern.compile("\n» ");
                matcher = pattern.matcher(ChatReceivedEvent.dfrpcMsg);
                int headerAmt = 0;
                while (matcher.find()) headerAmt++;
                if (headerAmt == 4) {
                    customStatus = ChatReceivedEvent.dfrpcMsg.replaceFirst("^.*\n.*\n\n» .*\n» ", "");
                    pattern = Pattern.compile("^.*");
                    matcher = pattern.matcher(customStatus);
                    while (matcher.find()) {
                        customStatus = matcher.group();
                    }
                }
            }

            // BUILD RICH PRESENCE
            presence.setState("Plot ID: " + id + " - " + node);
            presence.setDetails(name);

            if (ChatReceivedEvent.dfrpcMsg.startsWith("                                       \nYou are currently playing on:")) {
                presence.setSmallImage("modeplay", "Playing");
                presence.setLargeImage("diamondfirelogo", customStatus.equals("") ? "mcdiamondfire.com" : customStatus);
                mode = "play";
            }

            if (ChatReceivedEvent.dfrpcMsg.startsWith("                                       \nYou are currently building on:")) {
                presence.setSmallImage("modebuild", "Building");
                presence.setLargeImage("diamondfirelogo", "mcdiamondfire.com");
                mode = "build";
            }

            if (ChatReceivedEvent.dfrpcMsg.startsWith("                                       \nYou are currently coding on:")) {
                presence.setSmallImage("modedev", "Coding");
                presence.setLargeImage("diamondfirelogo", "mcdiamondfire.com");
                mode = "dev";
            }

        }

        if (!oldMode.equals(mode)) firstUpdate = true;

        if (firstUpdate) {
            time = OffsetDateTime.now();
        }
        presence.setStartTimestamp(time);
        oldMode = mode;

        if (ModConfig.getConfig().discordRPC) client.sendRichPresence(presence.build());
    }

}