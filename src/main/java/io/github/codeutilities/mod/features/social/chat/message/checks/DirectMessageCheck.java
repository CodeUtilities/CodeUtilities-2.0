package io.github.codeutilities.mod.features.social.chat.message.checks;

import io.github.codeutilities.mod.features.social.chat.ConversationTimer;
import io.github.codeutilities.mod.features.social.chat.message.Message;
import io.github.codeutilities.mod.features.social.chat.message.MessageCheck;
import io.github.codeutilities.mod.features.social.chat.message.MessageType;
import io.github.codeutilities.mod.features.streamer.StreamerModeHandler;
import io.github.codeutilities.mod.features.streamer.StreamerModeMessageCheck;

public class DirectMessageCheck extends MessageCheck implements StreamerModeMessageCheck {

    private static final String DIRECT_MESSAGE_REGEX = "^\\[(\\w{3,16}) → You] .+$";

    @Override
    public MessageType getType() {
        return MessageType.DIRECT_MESSAGE;
    }

    @Override
    public boolean check(Message message, String stripped) {
        return stripped.matches(DIRECT_MESSAGE_REGEX);
    }

    @Override
    public void onReceive(Message message) {
        // update conversation end timer
        if (ConversationTimer.currentConversation != null && usernameMatches(message, ConversationTimer.currentConversation)) {
            ConversationTimer.conversationUpdateTime = String.valueOf(System.currentTimeMillis());
        }
    }

    @Override
    public boolean streamerHideEnabled() {
        return StreamerModeHandler.hideDMs();
    }

    public static boolean usernameMatches(Message message, String username) {
        return message.getStripped().matches("^\\["+ username +" → You] .+$");
    }
}
