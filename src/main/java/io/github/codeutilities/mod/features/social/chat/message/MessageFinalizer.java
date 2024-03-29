package io.github.codeutilities.mod.features.social.chat.message;

import io.github.codeutilities.mod.features.social.chat.message.finalizers.DebugFinalizer;
import io.github.codeutilities.mod.features.social.chat.message.finalizers.MessageGrabberFinalizer;
import io.github.codeutilities.mod.features.social.chat.message.finalizers.StreamerModeFinalizer;
import io.github.codeutilities.sys.player.chat.MessageGrabber;

/**
 * Before a message is sent to the client, and after the message checks have been evaluated,
 * plus a check was accepted, all finalizers will be evaluated. The {@link MessageCheck} instance
 * accepted for this message can be retrieved using {@link Message#getCheck()}.
 */
public abstract class MessageFinalizer {

    private static final MessageFinalizer[] finalizers = new MessageFinalizer[]{
            new StreamerModeFinalizer(),
            new DebugFinalizer(),
            new MessageGrabberFinalizer()
    };

    /**
     * Use {@link Message#cancel()} to cancel the message
    */
    protected abstract void receive(Message message);

    public static void run(Message message) {
        for (MessageFinalizer finalizer : finalizers) {
            finalizer.receive(message);
        }
    }
}
