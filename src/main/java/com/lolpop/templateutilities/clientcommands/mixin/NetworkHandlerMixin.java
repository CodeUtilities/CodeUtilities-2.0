package com.lolpop.templateutilities.clientcommands.mixin;

import com.lolpop.templateutilities.clientcommands.ClientCommands;
import com.lolpop.templateutilities.clientcommands.impl.CommandCache;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.command.CommandSource;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SCommandListPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetHandler.class)
public class NetworkHandlerMixin {
    @Shadow private CommandDispatcher<CommandSource> commandDispatcher;

    @Inject(method = "handleCommandList", at = @At("RETURN"))
    private void handleCommandList(SCommandListPacket packetIn, CallbackInfo ci) {
        addCommands();
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onConstruct(Minecraft mcIn, Screen p_i46300_2_, NetworkManager networkManagerIn, GameProfile profileIn, CallbackInfo ci) {
        addCommands();
        CommandCache.build();
    }

    @Unique
    @SuppressWarnings("unchecked")
    private void addCommands() {
        ClientCommands.getCommands().registerCommands((CommandDispatcher) commandDispatcher);
    }
}
