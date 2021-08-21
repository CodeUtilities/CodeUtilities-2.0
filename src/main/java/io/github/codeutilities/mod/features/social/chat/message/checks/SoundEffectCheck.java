package io.github.codeutilities.mod.features.social.chat.message.checks;

import io.github.codeutilities.mod.features.social.chat.message.Message;
import io.github.codeutilities.mod.features.social.chat.message.MessageCheck;
import io.github.codeutilities.mod.features.social.chat.message.MessageType;

public class SoundEffectCheck extends MessageCheck{

    @Override
    public MessageType getType() {
        return MessageType.AWE_MESSAGE;
    }

    @Override
    public boolean check(Message message, String stripped) {
        return stripped.startsWith("(AWESLIB SYSTEM MESSAGE)");
    }

    @Override
    public void onReceive(Message message) {

    }
}
