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

    /*public static void mp3ToWav(File mp3Data, String name) throws UnsupportedAudioFileException, IOException {
        // open stream
        AudioInputStream mp3Stream = AudioSystem.getAudioInputStream(mp3Data);
        AudioFormat sourceFormat = mp3Stream.getFormat();
        // create audio format object for the desired stream/audio format
        // this is *not* the same as the file format (wav)
        AudioFormat convertFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                sourceFormat.getSampleRate(), 16,
                sourceFormat.getChannels(),
                sourceFormat.getChannels() * 2,
                sourceFormat.getSampleRate(),
                false);
        // create stream that delivers the desired format
        AudioInputStream converted = AudioSystem.getAudioInputStream(convertFormat, mp3Stream);
        // write stream into a file with file format wav
        AudioSystem.write(converted, AudioFileFormat.Type.WAVE, new File(FabricLoader.getInstance().getGameDir().resolve("aweslib").resolve(name).toString()));
    }*/

    public static void playSound(String name) throws LineUnavailableException {

        name = name.replace("/", "").replace(":", "").replace(".", "") + ".wav";

        Path pathOne = FabricLoader.getInstance().getGameDir().resolve("CodeUtilities").resolve("aweslib").resolve(name);
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
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }

}
