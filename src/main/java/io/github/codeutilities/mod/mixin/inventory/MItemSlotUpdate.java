package io.github.codeutilities.mod.mixin.inventory;

import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.mod.config.Config;
import io.github.codeutilities.mod.events.impl.ReceiveChatMessageEvent;
import io.github.codeutilities.mod.features.keybinds.FlightspeedToggle;
import io.github.codeutilities.mod.features.CPU_UsageText;
import io.github.codeutilities.sys.player.DFInfo;
import io.github.codeutilities.sys.networking.State;
import io.github.codeutilities.sys.hypercube.templates.TemplateStorageHandler;
import io.github.codeutilities.sys.hypercube.templates.TemplateUtils;
import io.github.codeutilities.sys.player.chat.ChatUtil;
import io.github.codeutilities.sys.player.chat.MessageGrabber;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class MItemSlotUpdate {
    final MinecraftClient mc = MinecraftClient.getInstance();
    private long lobbyTime = System.currentTimeMillis() - 1000;

    @Inject(method = "onScreenHandlerSlotUpdate", at = @At("HEAD"))
    public void onScreenHandlerSlotUpdate(ScreenHandlerSlotUpdateS2CPacket packet, CallbackInfo ci) {
        if (packet.getSyncId() == 0) {
            ItemStack stack = packet.getItemStack();
            if (TemplateUtils.isTemplate(stack)) {
                TemplateStorageHandler.addTemplate(stack);
            }

            CompoundTag nbt = stack.getOrCreateTag();
            CompoundTag display = nbt.getCompound("display");
            ListTag lore = display.getList("Lore", 8);
            if (mc.player == null) {
                return;
            }

            if (DFInfo.isOnDF() && stack.getName().getString().contains("◇ Game Menu ◇")
                    && lore.toText().getString().contains("\"Click to open the Game Menu.\"")
                    && lore.toText().getString().contains("\"Hold and type in chat to search.\"")) {

                DFInfo.currentState.sendLocate();

                // Auto fly
                if (Config.getBoolean("autofly")) {
                    if (System.currentTimeMillis() > lobbyTime) { // theres a bug with /fly running twice this is a temp fix.
                        mc.player.sendChatMessage("/fly");
                        MessageGrabber.hide(1);
                        lobbyTime = System.currentTimeMillis() + 1000;
                    }
                }

                CPU_UsageText.lagSlayerEnabled = false;

                // fs toggle
                FlightspeedToggle.fs_is_normal = true;
            }

            if (DFInfo.isOnDF() && mc.player.isCreative() && stack.getName().getString().contains("Player Event")
                    && lore.toText().getString().contains("\"Used to execute code when something\"")
                    && lore.toText().getString().contains("\"is done by (or happens to) a player.\"")
                    && lore.toText().getString().contains("\"Example:\"")) {

                DFInfo.currentState.sendLocate();
                DFInfo.plotCorner = mc.player.getPos().add(10, -50, -10);

                // Auto LagSlayer
                if (!CPU_UsageText.lagSlayerEnabled && Config.getBoolean("autolagslayer")) {
                    ChatUtil.executeCommandSilently("lagslayer");
                }

                // fs toggle
                FlightspeedToggle.fs_is_normal = true;


                }
            }
        }
    }
