package io.github.codeutilities.mod.features.social.chat.message;

import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.mod.features.social.chat.message.checks.*;

/**
 * Before a message is sent to the client, all message checks are evaluated
 */
public abstract class MessageCheck {

    // Define message checks here
    private static final MessageCheck[] checks = new MessageCheck[]{

            // General
            new LocateCheck(),
            new DirectMessageCheck(),
            new PlotAdCheck(),
            new JoinDiamondFireCheck(),
            new SoundEffectCheck(),

            // Lagslayer
            new LagslayerStartCheck(),
            new LagslayerStopCheck(),

            // Support
            new SupportCheck(),
            new SupportQuestionCheck(),
            new SupportAnswerCheck(),

            // Moderation
            new ModerationCheck(),
            new IncomingReportCheck(),
            new SilentPunishmentCheck(),
            new ScanningCheck(),
            new TeleportCheck(),
            new JoinFailCheck(),

            // Admin
            new SpiesCheck(),
            new BuycraftXUpdateCheck(),
            new AdminCheck(),

            // Custom regex
            new StreamerModeRegexCheck()

    };

    public abstract MessageType getType();

    public abstract boolean check(Message message, String stripped);

    /**
     * Use {@link Message#cancel()} to cancel the message
    */
    public abstract void onReceive(Message message);

    public static MessageType run(Message message) {
        for (MessageCheck check : checks) {
            if (check.check(message, message.getStripped())) {
                check.onReceive(message);
                message.setCheck(check);
                return check.getType();
            }
        }
        return MessageType.OTHER;
    }
}
