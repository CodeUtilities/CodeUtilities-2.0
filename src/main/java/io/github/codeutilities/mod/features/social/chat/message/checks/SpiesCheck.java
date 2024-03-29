package io.github.codeutilities.mod.features.social.chat.message.checks;

import io.github.codeutilities.mod.features.social.chat.message.Message;
import io.github.codeutilities.mod.features.social.chat.message.MessageCheck;
import io.github.codeutilities.mod.features.social.chat.message.MessageType;
import io.github.codeutilities.mod.features.streamer.StreamerModeHandler;
import io.github.codeutilities.mod.features.streamer.StreamerModeMessageCheck;

public class SpiesCheck extends MessageCheck implements StreamerModeMessageCheck {

    @Override
    public MessageType getType() {
        return MessageType.SPIES;
    }

    @Override
    public boolean check(Message message, String stripped) {
        // Hide spies (Session spy, Muted spy, DM spy)
        return stripped.startsWith("*");
    }

    @Override
    public void onReceive(Message message) {

    }

    @Override
    public boolean streamerHideEnabled() {
        return StreamerModeHandler.hideSpies();
    }
}
