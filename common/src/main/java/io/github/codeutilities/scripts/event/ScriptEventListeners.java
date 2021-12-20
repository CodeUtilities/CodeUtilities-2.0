package io.github.codeutilities.scripts.event;

import io.github.codeutilities.event.EventHandler;
import io.github.codeutilities.event.impl.KeyPressEvent;
import io.github.codeutilities.event.impl.ReceiveChatEvent;
import io.github.codeutilities.event.impl.ReloadCommandsEvent;
import io.github.codeutilities.event.impl.RenderGuiEvent;
import io.github.codeutilities.event.impl.SendChatEvent;
import io.github.codeutilities.event.impl.TickEvent;
import io.github.codeutilities.scripts.ScriptContext;
import io.github.codeutilities.scripts.ScriptHandler;
import io.github.codeutilities.scripts.types.ScriptNumber;
import io.github.codeutilities.scripts.types.ScriptText;
import io.github.codeutilities.util.ComponentUtil;

public class ScriptEventListeners {

    public static void init() {
        EventHandler.register(SendChatEvent.class, (event) -> {
            ScriptContext ctx = new ScriptContext();
            ctx.setVar("message", new ScriptText(event.getMessage()));
            ScriptHandler.triggerEvent(ScriptEventType.SEND_CHAT, ctx, event);
        });

        EventHandler.register(ReceiveChatEvent.class, (event) -> {
            try {
                ScriptContext ctx = new ScriptContext();
                String msg = ComponentUtil.toFormattedString(event.getMessage());
                ctx.setVar("message", new ScriptText(msg.replaceAll("§", "&")));
                ScriptHandler.triggerEvent(ScriptEventType.RECEIVE_CHAT, ctx, event);
            } catch (Throwable err) {
                err.printStackTrace();
            }
        });

        EventHandler.register(KeyPressEvent.class, (event) -> {
            ScriptContext ctx = new ScriptContext();
            ctx.setVar("key", new ScriptText(event.getKey().getName()));
            if (event.getAction() == 1) {
                ScriptHandler.triggerEvent(ScriptEventType.KEY_PRESS, ctx, event);
            } else if (event.getAction() == 0) {
                ScriptHandler.triggerEvent(ScriptEventType.KEY_RELEASE, ctx, event);
            }
        });

        EventHandler.register(TickEvent.class, (event) -> {
            ScriptHandler.triggerEvent(ScriptEventType.TICK, event);
        });

        EventHandler.register(ReloadCommandsEvent.class, (event) -> {
            ScriptHandler.triggerEvent(ScriptEventType.REGISTER_CMDS, event);
        });

        EventHandler.register(RenderGuiEvent.class, (event) -> {
            ScriptContext ctx = new ScriptContext();
            ctx.setVar("screenWidth", new ScriptNumber(event.scaledWindowWidth()));
            ctx.setVar("screenHeight", new ScriptNumber(event.scaledWindowHeight()));
            ScriptHandler.triggerEvent(ScriptEventType.RENDER_GUI, ctx, event);
        });
    }

}
