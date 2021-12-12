package io.github.codeutilities.event.impl;

import io.github.codeutilities.event.Event;

public record SendChatEvent(String message) implements Event {

}
