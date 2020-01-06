package com.lolpop.templateutilities.clientcommands.mixin;

import com.lolpop.templateutilities.clientcommands.ClientCommands;
import com.lolpop.templateutilities.clientcommands.LolpopClientCommandSource;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ClientSuggestionProvider.class)
public abstract class ClientCommandSourceMixin implements LolpopClientCommandSource
{
    @Shadow
    @Final
    private Minecraft mc;

    @Override
    public void sendFeedback(ITextComponent text) {
        mc.player.sendMessage(new StringTextComponent(text.getString()));
    }

    @Override
    public void sendError(ITextComponent text) {
        mc.player.sendMessage(new StringTextComponent(ChatFormatting.RED + text.getString()));

    }
}