package io.github.codeutilities.mixin.item;

import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.config.ModConfig;
import io.github.codeutilities.template.TemplateStorageHandler;
import io.github.codeutilities.util.DFInfo;
import io.github.codeutilities.util.TemplateUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.util.registry.*;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@Mixin(ClientPlayNetworkHandler.class)
public class MixinItemSlotUpdate {

    MinecraftClient mc = MinecraftClient.getInstance();

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

            if (mc.player.isCreative() && stack.getName().getString().contains("Values")
                && lore.toText().getString().contains("\"Right click this to obtain values. Types include\"")
                && lore.toText().getString().contains("\"numbers, variables, text, sound effects, game\"")
                && lore.toText().getString().contains("\"values, potion effects, and spawn eggs.\"")) {

                if (DFInfo.currentState != DFInfo.State.DEV) {
                    DFInfo.currentState = DFInfo.State.DEV;
                    DFInfo.plotCorner = mc.player.getPos().add(10, -50, -10);

                    new Thread(() -> {
                        try {
                            Thread.sleep(10);
                            if(ModConfig.getConfig().autoRC) mc.player.sendChatMessage("/rc");
                            if(ModConfig.getConfig().autotime) mc.player.sendChatMessage("/time " + ModConfig.getConfig().autotimeval);
                        } catch (Exception e) {
                            CodeUtilities.log(Level.ERROR, "Error while executing the task!");
                            e.printStackTrace();
                        }
                    }).start();
                }
            }
        }
    }


}
