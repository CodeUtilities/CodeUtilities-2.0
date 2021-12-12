package io.github.codeutilities.event.impl;

import io.github.codeutilities.event.Event;
import io.github.codeutilities.event.type.Cancellable;

public class SendChatEvent implements Event, Cancellable {

    private final String message;
    private boolean cancelled = false;

    public SendChatEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
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
