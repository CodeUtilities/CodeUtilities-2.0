package io.github.codeutilities.mod.mixin.render;

import io.github.codeutilities.mod.config.Config;
import io.github.codeutilities.sys.player.DFInfo;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatScreen.class)
public class MChatScreen {
    private static String[] allowedLongCmds = new String[] {
            "/plot ",
            "/text ",
            "/txt ",
            "/item ",
            "/rename ",
            "/importmap "
    };

    @Shadow protected TextFieldWidget chatField;

    @Inject(method = "onChatFieldUpdate", at = @At("TAIL"))
    public void onChatFieldUpdate(String chatText, CallbackInfo ci) {
        if (DFInfo.isOnDF() && Config.getBoolean("longerDFCmds")) {
            boolean flag = false;
            for (int i = 0; i < allowedLongCmds.length; i++) {
                if (chatText.startsWith(allowedLongCmds[i])) {
                    this.chatField.setMaxLength(2048);
                    flag = true;
                }
            }

            if (!flag) this.chatField.setMaxLength(256);
        } else {
            this.chatField.setMaxLength(256);
        }
    }
}
