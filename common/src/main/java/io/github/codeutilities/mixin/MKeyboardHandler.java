package io.github.codeutilities.mixin;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.InputConstants.Key;
import io.github.codeutilities.event.EventHandler;
import io.github.codeutilities.event.impl.KeyPressEvent;
import net.minecraft.client.KeyboardHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public class MKeyboardHandler {

    @Inject(method = "keyPress", at = @At("HEAD"), cancellable = true)
    private void keyPress(long window, int i, int j, int k, int m, CallbackInfo ci) {
        Key key = InputConstants.getKey(i,j);

        KeyPressEvent event = new KeyPressEvent(key,k);
        EventHandler.invoke(event);
        if (event.isCancelled()) {
            ci.cancel();
        }
    }

}
