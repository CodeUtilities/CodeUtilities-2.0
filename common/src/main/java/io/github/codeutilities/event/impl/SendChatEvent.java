package io.github.codeutilities.event.impl;

import io.github.codeutilities.event.Event;
import io.github.codeutilities.event.type.Cancellable;

public class SendChatEvent extends Cancellable implements Event {

    private final String message;

    public SendChatEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
