package io.github.codeutilities.scripts.event;

import io.github.codeutilities.event.EventHandler;
import io.github.codeutilities.event.impl.SendChatEvent;
import io.github.codeutilities.scripts.ScriptContext;
import io.github.codeutilities.scripts.ScriptHandler;

public class ScriptEventListeners {

    public static void init() {
        EventHandler.register(SendChatEvent.class, (event) -> {
            ScriptContext ctx = new ScriptContext();
            ctx.setVar("message", event.message());
            ScriptHandler.triggerEvent(ScriptEventType.SEND_CHAT, ctx);
        });
    }

}
