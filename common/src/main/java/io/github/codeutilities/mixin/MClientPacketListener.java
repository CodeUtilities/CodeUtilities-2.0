package io.github.codeutilities.mixin;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.CommandNode;
import io.github.codeutilities.event.EventHandler;
import io.github.codeutilities.event.impl.ReloadCommandsEvent;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.protocol.game.ClientboundCommandsPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class MClientPacketListener {

    @Shadow public abstract CommandDispatcher<SharedSuggestionProvider> getCommands();

    @Inject(method = "handleCommands", at = @At("RETURN"))
    private void handleCommands(ClientboundCommandsPacket packet, CallbackInfo ci) {
        CommandDispatcher<SharedSuggestionProvider> vanillaCommands = new CommandDispatcher<>();
        for (CommandNode<SharedSuggestionProvider> child : getCommands().getRoot().getChildren()) {
            vanillaCommands.getRoot().addChild(child);
        }
        EventHandler.invoke(new ReloadCommandsEvent(getCommands(),vanillaCommands));
    }

}
