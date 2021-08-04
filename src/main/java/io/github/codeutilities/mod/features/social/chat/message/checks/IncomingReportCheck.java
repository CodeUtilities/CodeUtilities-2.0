package io.github.codeutilities.mod.features.social.chat.message.checks;

import io.github.codeutilities.mod.features.social.chat.message.Message;
import io.github.codeutilities.mod.features.social.chat.message.MessageCheck;
import io.github.codeutilities.mod.features.social.chat.message.MessageType;

public class IncomingReportCheck extends MessageCheck {

    @Override
    public MessageType getType() {
        return MessageType.INCOMING_REPORT;
    }

    @Override
    public boolean check(Message message, String stripped) {
        return stripped.startsWith("! Incoming Report ");
    }
}
