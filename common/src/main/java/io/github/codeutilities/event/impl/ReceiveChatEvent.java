package io.github.codeutilities.event.impl;

import io.github.codeutilities.event.Event;
import io.github.codeutilities.event.type.Cancellable;
import net.minecraft.network.chat.Component;

public class ReceiveChatEvent implements Event, Cancellable {

    private final Component message;
    private boolean cancelled = false;

    public ReceiveChatEvent(Component message) {
        this.message = message;
    }

    public Component getMessage() {
        return message;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
