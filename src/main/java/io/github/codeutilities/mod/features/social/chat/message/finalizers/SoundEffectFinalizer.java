package io.github.codeutilities.mod.features.social.chat.message.finalizers;

import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.mod.config.Config;
import io.github.codeutilities.mod.features.social.chat.message.Message;
import io.github.codeutilities.mod.features.social.chat.message.MessageFinalizer;
import io.github.codeutilities.mod.features.social.chat.message.MessageType;
import io.github.codeutilities.sys.aweslib.SoundPlayer;
import io.github.codeutilities.sys.aweslib.AWEManager;

import org.apache.logging.log4j.Level;

import javax.sound.sampled.LineUnavailableException;

    public class SoundEffectFinalizer extends MessageFinalizer {

        @Override
        protected void receive(Message message) {
            message.typeIs(MessageType.AWE_MESSAGE);
            String stripped = message.getStripped();
            if(stripped.startsWith("(AWESLIB SYSTEM MESSAGE)") && Config.getBoolean("soundlib")) { // Every message was counted as a aweslib message without this.
                message.cancel();
                if (AWEManager.token.equals("empty")) {
                    if (stripped.contains("Token:")) {
                        String tok = stripped.substring(32);
                        AWEManager.token = tok;
                        AWEManager.tokenIsSet = true;
                    }
                }
                if (stripped.contains("Download Sound")) {
                    String sound = stripped.substring(41);
                    AWEManager.sounds.add(sound);
                }
                if (stripped.contains("Play Sound")) {
                    AWEManager.downloaded = true;
                    String sound = stripped.substring(37);
                    CodeUtilities.log(Level.INFO, "Playing sound: " + sound);
                    try {
                        SoundPlayer.playSound(sound);
                    } catch (LineUnavailableException e) {
                        e.printStackTrace();
                    }
                    //SoundPlayer.playSnd(sound);
                    //}
                }
                if (!AWEManager.downloaded) {
                    AWEManager.stateChange();
                }
                //Main.log(Level.INFO, "AWESLIB SYSTEM MESSAGE DETECTED.");


            }
        }
    }
