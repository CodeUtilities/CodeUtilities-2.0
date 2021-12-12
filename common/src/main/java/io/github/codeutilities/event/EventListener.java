package io.github.codeutilities.event;

/**
 * A simple event listener for listening to CodeUtilities related events.
 * See {@link EventHandler#register(Class, EventListener)} for subscribing
 * to events.
 * @param <T> The event to subscribe to.
 */
public interface EventListener<T extends Event> {

    void fire(T event);
}