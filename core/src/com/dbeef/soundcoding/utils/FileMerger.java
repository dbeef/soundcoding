package com.dbeef.soundcoding.utils;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.io.SequenceInputStream;
import java.util.LinkedList;

/**
 * Created by dbeef on 26.05.17.
 */
public class FileMerger {
    public void merge(LinkedList<String> fileNames) {
        String wavFile1 = fileNames.get(0) + ".wav";
        fileNames.remove(wavFile1);

        for (String fileName : fileNames) {

            System.out.println("Merging " + wavFile1 + " with " + fileName );

            String wavFile2 = fileName + ".wav";

            try {
                AudioInputStream clip1 = AudioSystem.getAudioInputStream(new File(wavFile1));
                AudioInputStream clip2 = AudioSystem.getAudioInputStream(new File(wavFile2));

                AudioInputStream appendedFiles =
                        new AudioInputStream(
                                new SequenceInputStream(clip1, clip2),
                                clip1.getFormat(),
                                clip1.getFrameLength() + clip2.getFrameLength());

                new File(wavFile1).delete();

                AudioSystem.write(appendedFiles,
                        AudioFileFormat.Type.WAVE,
                        new File(wavFile1));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
