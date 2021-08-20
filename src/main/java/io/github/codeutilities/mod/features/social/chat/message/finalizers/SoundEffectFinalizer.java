package io.github.codeutilities.mod.features.social.chat.message.finalizers;

import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.mod.features.social.chat.message.Message;
import io.github.codeutilities.mod.features.social.chat.message.MessageFinalizer;
import io.github.codeutilities.mod.features.social.chat.message.MessageType;
import io.github.codeutilities.sys.aweslib.SoundPlayer;
import io.github.codeutilities.sys.aweslib.awemanager;

import org.apache.logging.log4j.Level;

import javax.sound.sampled.LineUnavailableException;

    public class SoundEffectFinalizer extends MessageFinalizer {

        @Override
        protected void receive(Message message) {
            message.typeIs(MessageType.SOUND_EFFECT);
            String stripped = message.getStripped();
            if(stripped.contains("(AWESLIB SYSTEM MESSAGE)")) {
                message.cancel();
                if(awemanager.Token.equals("empty")) {
                    if(stripped.contains("Token:")) {
                        String tok = stripped.substring(32);
                        awemanager.Token = tok;
                        awemanager.TokenIsSet = true;
                        CodeUtilities.log("Set token to " + tok);
                    }
                }
                if(stripped.contains("Download Sound")) {
                    String sound = stripped.substring(41);
                    awemanager.sounds.add(sound);
                }
                if(stripped.contains("Play Sound")) {
                    awemanager.Downloaded = true;
                    String sound = stripped.substring(37);
                    CodeUtilities.log(Level.INFO, "Playing sound: " + sound);
                    try {
                        SoundPlayer.playSnd(sound);
                    } catch (LineUnavailableException e) {
                        e.printStackTrace();
                    }
                    //SoundPlayer.playSnd(sound);
                    //}
                }
                if(!awemanager.Downloaded) {
                    awemanager.stateChange();
                }
                //Main.log(Level.INFO, "AWESLIB SYSTEM MESSAGE DETECTED.");
            }


        }
    }
