package io.github.codeutilities.mod.mixin.message;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.mod.config.Config;
import io.github.codeutilities.mod.events.interfaces.ChatEvents;
import io.github.codeutilities.mod.features.CPU_UsageText;
import io.github.codeutilities.mod.features.keybinds.FlightspeedToggle;
import io.github.codeutilities.mod.features.social.chat.message.Message;
import io.github.codeutilities.sys.networking.State;
import io.github.codeutilities.sys.networking.WebUtil;
import io.github.codeutilities.sys.player.DFInfo;
import io.github.codeutilities.sys.player.chat.ChatUtil;
import io.github.codeutilities.sys.util.VersionUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.MessageType;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.text.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;

@Mixin(ClientPlayNetworkHandler.class)
public class MMessageListener {
    private static long lastPatchCheck = 0;
    private static long lastBuildCheck = 0;
    private final MinecraftClient minecraftClient = MinecraftClient.getInstance();
    private boolean motdShown = false;
    private final ChatEvents invoker = ChatEvents.RECEIVE_MESSAGE.invoker();

    @Inject(method = "onGameMessage", at = @At("HEAD"), cancellable = true)
    private void onGameMessage(GameMessageS2CPacket packet, CallbackInfo ci) {
        if (DFInfo.isOnDF()) {
            if (packet.getLocation() == MessageType.CHAT || packet.getLocation() == MessageType.SYSTEM) {
                if (RenderSystem.isOnRenderThread()) {
                    if (invoker.receive(new Message(packet, ci)).equals(ActionResult.SUCCESS)) ci.cancel();
                    try {
                        this.updateVersion(packet.getMessage());
                        this.updateState(packet.getMessage());
                    } catch (Exception e) {
                        e.printStackTrace();
                        CodeUtilities.log(Level.ERROR, "Error while trying to parse the chat text!");
                    }
                }
            }
        }
    }

    @Inject(method = "onTitle", at = @At("HEAD"), cancellable = true)
    private void onTitle(TitleS2CPacket packet, CallbackInfo ci) {
        TitleS2CPacket.Action action = packet.getAction();
        if (minecraftClient.player == null) return;
        if (action == TitleS2CPacket.Action.ACTIONBAR) {
            if (packet.getText().getString().matches("^CPU Usage: \\[▮▮▮▮▮▮▮▮▮▮▮▮▮▮▮▮▮▮▮▮\\] \\(.*%\\)$")) {
                if (Config.getBoolean("cpuOnScreen")) {
                    CPU_UsageText.updateCPU(packet);
                    ci.cancel();
                }
            }
        }
    }

    private void updateVersion(Text component) {
        if (minecraftClient.player == null) return;

        String text = component.getString();

        if (text.matches("Current patch: .*\\. See the patch notes with /patch!")) {
            try {
                long time = System.currentTimeMillis() / 1000L;
                if (time - lastPatchCheck > 2) {
                    String patchText = text.replaceAll("Current patch: (.*)\\. See the patch notes with /patch!", "$1");

                    if (!motdShown) {
                        try {
                            String str = WebUtil.getString("https://codeutilities.github.io/data/motd.txt");
                            for (String string : str.split("\n")) {
                                minecraftClient.player.sendMessage(new LiteralText(string).styled(style -> style.withColor(TextColor.fromFormatting(Formatting.AQUA))), false);
                            }

                            if (!CodeUtilities.BETA) {
                                int latestVersion = VersionUtil.getLatestVersion();
                                int currentVersion = VersionUtil.getCurrentVersionInt();
                                int versionsBehind = latestVersion - currentVersion;

                                if (versionsBehind > 10) {
                                    MutableText message = new LiteralText("")
                                            .append(new LiteralText(String.format("You are currently on build #%s of CodeUtilities, which is %s versions behind the latest (%s). ",
                                                    currentVersion, versionsBehind, latestVersion))
                                                    .styled(style -> style.withColor(Formatting.YELLOW)))
                                            .append(new LiteralText("Click here to download the latest version!")
                                                    .styled(style -> {
                                                        style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://codeutilities.github.io"));
                                                        style.withColor(Formatting.AQUA);
                                                        return style;
                                                    }));

                                    minecraftClient.player.sendMessage(message, false);

                                }
                            }

                        } catch (IOException ignored) {
                        }

                        motdShown = true;
                    }

                    DFInfo.isPatchNewer(patchText, "0"); //very lazy validation lol
                    DFInfo.patchId = patchText;
                    DFInfo.currentState.sendLocate();
                    CodeUtilities.log(Level.INFO, "DiamondFire Patch " + DFInfo.patchId + " detected!");

                    lastPatchCheck = time;

                    // update state on server join
                    DFInfo.currentState.setInSession(false);

                    // auto chat local
                    if (Config.getBoolean("autoChatLocal")) {
                        //Deprecated ChatUtil.executeCommandSilently("c 1");
                        ChatUtil.executeCommandSilently("chat local");
                    }
                }
            } catch (Exception e) {
                CodeUtilities.log(Level.INFO, "Error on parsing patch number!");
                e.printStackTrace();
            }
        }
    }

    private void updateState(Text component) {
        if (minecraftClient.player == null) return;

        String text = component.getString();

        // Flight speed
        if (text.matches("^Set flight speed to: \\d+% of default speed\\.$") && !text.matches("^Set flight speed to: 100% of default speed\\.$")) {
            FlightspeedToggle.fs_is_normal = false;
        }

        // Play Mode
        if (text.matches("^Joined game: .* by .*$")) {
            DFInfo.currentState.sendLocate();

            // Auto LagSlayer
            System.out.println(CPU_UsageText.lagSlayerEnabled);
            if (!CPU_UsageText.lagSlayerEnabled && Config.getBoolean("autolagslayer")) {
                ChatUtil.executeCommandSilently("lagslayer");
            }

            // fs toggle
            FlightspeedToggle.fs_is_normal = true;
        }

        // Enter Session
        if (text.matches("^You have entered a session with .*\\.$")) {
            if (!DFInfo.currentState.isInSession()) {
                DFInfo.currentState.setInSession(true);
                DFInfo.currentState.sendLocate();
            }
        }

        // End Session
        if (text.matches("^" + minecraftClient.player.getName().asString() + " finished a session with .*\\. ▶ .*$")) {
            if (DFInfo.currentState.isInSession()) {
                DFInfo.currentState.setInSession(false);
                DFInfo.currentState.sendLocate();
            }
        }
        if (text.matches("^Your session with .* has ended\\.$")) {
            if (DFInfo.currentState.isInSession()) {
                DFInfo.currentState.setInSession(false);
                DFInfo.currentState.sendLocate();
            }
        }

        // Build Mode
        if (minecraftClient.player.isCreative() && text.matches("^» You are now in build mode\\.$")) {
            if (DFInfo.currentState.getMode() != State.Mode.BUILD) {
                DFInfo.currentState.sendLocate();
            }

            // Auto LagSlayer
            if (!CPU_UsageText.lagSlayerEnabled && Config.getBoolean("autolagslayer")) {
                ChatUtil.executeCommandSilently("lagslayer");
            }

            // fs toggle
            FlightspeedToggle.fs_is_normal = true;

            long time = System.currentTimeMillis() / 1000L;
            if (time - lastBuildCheck > 1) {
                new Thread(() -> {
                    try {
                        Thread.sleep(20);
                        if (Config.getBoolean("autotime")) {
                            ChatUtil.executeCommandSilently("time " + Config.getInteger("autotimeval"));
                        }
                        if (Config.getBoolean("autonightvis")) {
                            ChatUtil.executeCommandSilently("nightvis");
                        }
                    } catch (Exception e) {
                        CodeUtilities.log(Level.ERROR, "Error while executing the task!");
                        e.printStackTrace();
                    }
                }).start();

                lastBuildCheck = time;
            }
        }

        // Dev Mode (more moved to MixinItemSlotUpdate)
        if (minecraftClient.player.isCreative() && text.matches("^» You are now in dev mode\\.$")) {
            // fs toggle
            FlightspeedToggle.fs_is_normal = true;

            new Thread(() -> {
                try {
                    Thread.sleep(10);
                    if (Config.getBoolean("autoRC")) {
                        CodeUtilities.MC.player.sendChatMessage("/rc");
                    }
                    if (Config.getBoolean("autotime")) {
                        ChatUtil.executeCommandSilently("time " + Config.getLong("autotimeval"));
                    }
                    if (Config.getBoolean("autonightvis")) {
                        ChatUtil.executeCommandSilently("nightvis");
                    }
                } catch (Exception e) {
                    CodeUtilities.log(Level.ERROR, "Error while executing the task!");
                    e.printStackTrace();
                }
            }).start();

        }
    }
}