package io.github.codeutilities.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.codeutilities.event.EventHandler;
import io.github.codeutilities.event.impl.RenderGuiEvent;
import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class MGui {

    @Shadow private int screenWidth;

    @Shadow private int screenHeight;

    @Inject(method = "render", at = @At("HEAD"))
    private void render(PoseStack poseStack, float tickDelta, CallbackInfo ci) {
        EventHandler.invoke(new RenderGuiEvent(poseStack,tickDelta, screenWidth, screenHeight));
    }
}
