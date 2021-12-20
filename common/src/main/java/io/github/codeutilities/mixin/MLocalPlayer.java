package io.github.codeutilities.mixin;

import com.mojang.brigadier.StringReader;
import io.github.codeutilities.commands.CommandHandler;
import io.github.codeutilities.event.EventHandler;
import io.github.codeutilities.event.impl.SendChatEvent;
import io.github.codeutilities.event.impl.TickEvent;
import io.github.codeutilities.scripts.ScriptHandler;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(LocalPlayer.class)
public abstract class MLocalPlayer {

    @Inject(method = "chat", at = @At("HEAD"), cancellable = true)
    private void chat(String msg, CallbackInfo ci) {
        try {
            StringReader reader = new StringReader(msg);
            if (reader.canRead() && reader.read() == '/') {
                if (CommandHandler.dispatch(reader)) {
                    ci.cancel();
                }
                if (ScriptHandler.dispatchCmd(reader)) {
                    ci.cancel();
                }
            }
            SendChatEvent evn = new SendChatEvent(msg);
            EventHandler.invoke(evn);
            if (evn.isCancelled()) {
                ci.cancel();
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        EventHandler.invoke(new TickEvent());
    }

}