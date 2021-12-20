package io.github.codeutilities.mixin;

import io.github.codeutilities.event.EventHandler;
import io.github.codeutilities.event.impl.ReceiveChatEvent;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatComponent.class)
public class MChatComponent {

    @Inject(method = "addMessage(Lnet/minecraft/network/chat/Component;IIZ)V", at = @At("HEAD"), cancellable = true)
    private void addMessage(Component component, int i, int j, boolean bl, CallbackInfo ci) {
        ReceiveChatEvent event = new ReceiveChatEvent(component);
        EventHandler.invoke(event);
        if (event.isCancelled()) {
            ci.cancel();
        }
    }

}
