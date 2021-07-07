package io.github.codeutilities.mixin.item;

import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.config.Config;
import io.github.codeutilities.events.register.ReceiveChatMessageEvent;
import io.github.codeutilities.features.keybinds.FlightspeedToggle;
import io.github.codeutilities.util.gui.CPU_UsageText;
import io.github.codeutilities.util.networking.DFInfo;
import io.github.codeutilities.util.networking.State;
import io.github.codeutilities.util.templates.TemplateStorageHandler;
import io.github.codeutilities.util.templates.TemplateUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class MixinItemSlotUpdate {
    final MinecraftClient mc = MinecraftClient.getInstance();
    private long lobbyTime = System.currentTimeMillis() - 1000;
    private long lastDevCheck = 0;

    @Inject(method = "onScreenHandlerSlotUpdate", at = @At("HEAD"))
    public void onScreenHandlerSlotUpdate(ScreenHandlerSlotUpdateS2CPacket packet, CallbackInfo ci) {
        if (packet.getSyncId() == 0) {
            ItemStack stack = packet.getItemStack();
            if (TemplateUtils.isTemplate(stack)) {
                TemplateStorageHandler.addTemplate(stack);
            }

            NbtCompound nbt = stack.getOrCreateTag();
            NbtCompound display = nbt.getCompound("display");
            NbtList lore = display.getList("Lore", 8);
            if (mc.player == null) {
                return;
            }

            //TODO: figure out what NbtList.toText() is in 1.17 and update it
            if (DFInfo.isOnDF() && stack.getName().getString().contains("◇ Game Menu ◇")
                    && lore.toString().contains("\"Click to open the Game Menu.\"")
                    && lore.toString().contains("\"Hold and type in chat to search.\"")) {

                if (DFInfo.currentState.getMode() != State.Mode.SPAWN) {
                    DFInfo.currentState.sendLocate();

                    // Auto fly
                    if (Config.getBoolean("autofly")) {
                        if (System.currentTimeMillis() > lobbyTime) { // theres a bug with /fly running twice this is a temp fix.
                            mc.player.sendChatMessage("/fly");
                            ReceiveChatMessageEvent.cancelFlyMsg = true;
                            lobbyTime = System.currentTimeMillis() + 1000;
                        }

                    }

                    CPU_UsageText.lagSlayerEnabled = false;

                    // fs toggle
                    FlightspeedToggle.fs_is_normal = true;
                }
            }

            if (DFInfo.isOnDF() && mc.player.isCreative() && stack.getName().getString().contains("Player Event")
                    && lore.toString().contains("\"Used to execute code when something\"")
                    && lore.toString().contains("\"is done by (or happens to) a player.\"")
                    && lore.toString().contains("\"Example:\"")) {

                if (DFInfo.currentState.getMode() != State.Mode.DEV) {
                    DFInfo.currentState.sendLocate();
                    DFInfo.plotCorner = mc.player.getPos().add(10, -50, -10);

                    // Auto LagSlayer
                    if (!CPU_UsageText.lagSlayerEnabled && Config.getBoolean("autolagslayer")) {
                        mc.player.sendChatMessage("/lagslayer");
                        ReceiveChatMessageEvent.cancelLagSlayerMsg = true;
                    }

                    // fs toggle
                    FlightspeedToggle.fs_is_normal = true;

                    long time = System.currentTimeMillis() / 1000L;
                    if (time - lastDevCheck > 1) {

                        new Thread(() -> {
                            try {
                                Thread.sleep(10);
                                if (Config.getBoolean("autoRC")) {
                                    mc.player.sendChatMessage("/rc");
                                }
                                if (Config.getBoolean("autotime")) {
                                    mc.player.sendChatMessage("/time " + Config.getLong("autotimeval"));
                                    ReceiveChatMessageEvent.cancelTimeMsg = true;
                                }
                                if (Config.getBoolean("autonightvis")) {
                                    mc.player.sendChatMessage("/nightvis");
                                    ReceiveChatMessageEvent.cancelNVisionMsg = true;
                                }
                            } catch (Exception e) {
                                CodeUtilities.log(Level.ERROR, "Error while executing the task!");
                                e.printStackTrace();
                            }
                        }).start();

                        lastDevCheck = time;
                    }
                }
            }
        }
    }

}
