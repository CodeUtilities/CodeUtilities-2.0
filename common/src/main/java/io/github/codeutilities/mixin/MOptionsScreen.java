package io.github.codeutilities.mixin;

import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.config.menu.ConfigScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.OptionsScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OptionsScreen.class)
public class MOptionsScreen extends Screen {

    protected MOptionsScreen(Component component) {
        super(component);
    }

    @Inject(method = "init()V", at = @At("RETURN"))
    protected void init(CallbackInfo callbackInfo) {
        this.addRenderableWidget(new Button(this.width / 2 - 75, this.height / 6 + 144 - 6, 150, 20, new TextComponent("CodeUtilities"), (buttonWidget) -> CodeUtilities.MC.setScreen(ConfigScreen.getScreen(CodeUtilities.MC.screen))));
    }
}