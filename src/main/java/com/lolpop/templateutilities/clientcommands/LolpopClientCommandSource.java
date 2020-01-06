package com.lolpop.templateutilities.clientcommands;

import net.minecraft.command.ICommandSource;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.util.text.ITextComponent;

public interface LolpopClientCommandSource extends ISuggestionProvider {
    void sendFeedback(ITextComponent text);
    void sendError(ITextComponent text);
}