package io.github.codeutilities.mixin;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.RootCommandNode;
import io.github.codeutilities.commands.Command;
import io.github.codeutilities.commands.CommandHandler;
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

    @Inject(method = "handleCommands", at = @At("HEAD"))
    private void handleCommands(ClientboundCommandsPacket packet, CallbackInfo ci) {
        // inject our own command info into the root command node.
        RootCommandNode<SharedSuggestionProvider> rootCommandNode = packet.getRoot();
        CommandHandler.dispatcher.getRoot().getChildren().forEach(rootCommandNode::addChild);
    }

}
