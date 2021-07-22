package io.github.codeutilities.mod.mixin.packet;

import io.github.codeutilities.sys.util.networking.TPSUtil;
import net.minecraft.client.network.ClientLoginNetworkHandler;
import net.minecraft.network.packet.s2c.login.LoginSuccessS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLoginNetworkHandler.class)
public class MixinClientLoginNetworkHandler {
    @Inject(method = "onLoginSuccess", at = @At("RETURN"))
    public void onLoginSuccess(LoginSuccessS2CPacket packet, CallbackInfo ci) {
        TPSUtil.reset();
    }
}
