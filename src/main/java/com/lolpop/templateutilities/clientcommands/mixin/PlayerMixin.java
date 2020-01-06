package com.lolpop.templateutilities.clientcommands.mixin;

import com.lolpop.templateutilities.clientcommands.LolpopClientCommandSource;
import com.lolpop.templateutilities.clientcommands.impl.CommandCache;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.command.CommandException;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class PlayerMixin {
    @Final
    @Shadow
    protected Minecraft mc;

    @Final
    @Shadow
    public ClientPlayNetHandler connection;

    @Shadow
    public abstract void sendMessage(ITextComponent text_1);

    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    private void onChatMessage(String msg, CallbackInfo info) {
        if (msg.length() < 2 || !msg.startsWith("/")) return;
        if (!CommandCache.hasCommand(msg.substring(1).split(" ")[0])) return;
        boolean cancel = false;
        try {
            System.out.println("CommandException");
            int result = CommandCache.execute(
                    msg.substring(1), (LolpopClientCommandSource) new ClientSuggestionProvider(connection, mc)
            );
            if (result != 0)
                // Prevent sending the message
                cancel = true;
        } catch (CommandException e) {
            sendMessage(new StringTextComponent(ChatFormatting.RED + e.getMessage()));
            cancel = true;
        } catch (CommandSyntaxException e) {
            sendMessage(new StringTextComponent(ChatFormatting.RED + e.getMessage()));
            cancel = true;
        } catch (Exception e) {
            sendMessage( new StringTextComponent(ChatFormatting.RED + new TranslationTextComponent("command.failed").getString()));
            cancel = true;
        }

        if (cancel)
            info.cancel();
    }
}
