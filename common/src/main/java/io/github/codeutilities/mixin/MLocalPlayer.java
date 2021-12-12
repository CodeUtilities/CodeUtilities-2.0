package io.github.codeutilities.mixin;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.commands.CommandHandler;
import io.github.codeutilities.scripts.ScriptContext;
import io.github.codeutilities.scripts.event.ScriptEventType;
import io.github.codeutilities.scripts.ScriptHandler;
import java.util.Objects;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.commands.SharedSuggestionProvider;
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
                CommandDispatcher<SharedSuggestionProvider> dispatcher = Objects.requireNonNull(CodeUtilities.MC.getConnection()).getCommands();
                if (dispatcher.execute(reader, null) == CommandHandler.CANCEL_MESSAGE) {
                    ci.cancel();
                }
            }
            ScriptContext ctx = new ScriptContext();
            ctx.setVar("message",msg);
            ScriptHandler.triggerEvent(ScriptEventType.SEND_CHAT, ctx);
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

}