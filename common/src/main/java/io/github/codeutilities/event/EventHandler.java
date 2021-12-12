package io.github.codeutilities.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventHandler {

    private static final Map<Class<? extends Event>, List<EventListener<Event>>> eventListeners = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static <T extends Event> void register(Class<T> tClass, EventListener<T> listener) {
        eventListeners.putIfAbsent(tClass, new ArrayList<>());
        eventListeners.get(tClass).add((EventListener<Event>) listener);
    }

    public static void invoke(Event event) {
        eventListeners.get(event.getClass()).forEach(f -> f.fire(event));
    }

}
