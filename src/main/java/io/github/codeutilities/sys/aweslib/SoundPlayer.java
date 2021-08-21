package io.github.codeutilities.sys.aweslib;


import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import javax.sound.sampled.*;
import javax.sound.sampled.DataLine.Info;

import static javax.sound.sampled.AudioSystem.getAudioInputStream;
import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;

public class SoundPlayer {

    public static void playSound(String name) throws LineUnavailableException {

        name = AWEUtils.legalizeUrl(name);

        Path pathOne = FabricLoader.getInstance().getGameDir().resolve("CodeUtilities/aweslib/" + name);
        File fileOne = pathOne.toFile();
        Clip clip = AudioSystem.getClip();

        if (clip != null && clip.isActive()) {
            return;
        }
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(fileOne);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(gainControl.getMaximum() * 1);
            clip.start();
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            AWEUtils.sendMessage("Something weird has happened whilst playing sound.");
        }
    }

}
