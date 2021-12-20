package io.github.codeutilities.event.impl;

import com.mojang.brigadier.CommandDispatcher;
import io.github.codeutilities.event.Event;
import net.minecraft.commands.SharedSuggestionProvider;

public record ReloadCommandsEvent(CommandDispatcher<SharedSuggestionProvider> modifiableDispatcher, CommandDispatcher<SharedSuggestionProvider> vanillaDispatcher) implements Event {
}
