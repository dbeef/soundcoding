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
    public String finalFileName;

    public void merge(LinkedList<String> fileNames) {
        String wavFile1 = fileNames.get(0) + ".wav";
        fileNames.removeFirst();

        boolean firstTime = true;
        boolean t = true;
        String fa = "fa.wav";
        String tr = "tr.wav";

        for (String fileName : fileNames) {

            System.out.println("Merging " + wavFile1 + " with " + fileName);

            String wavFile2 = fileName + ".wav";

            try {
                AudioInputStream clip1 = null;
                AudioInputStream clip2 = null;
                if (firstTime) {
                    clip1 = AudioSystem.getAudioInputStream(new File(wavFile1));
                    clip2 = AudioSystem.getAudioInputStream(new File(wavFile2));
                } else {
                    if (t)
                        clip1 = AudioSystem.getAudioInputStream(new File(tr));
                    else
                        clip1 = AudioSystem.getAudioInputStream(new File(fa));

                    clip2 = AudioSystem.getAudioInputStream(new File(wavFile2));
                }

                AudioInputStream appendedFiles =
                        new AudioInputStream(
                                new SequenceInputStream(clip1, clip2),
                                clip1.getFormat(),
                                clip1.getFrameLength() + clip2.getFrameLength());

                if(t) {
                    AudioSystem.write(appendedFiles,
                            AudioFileFormat.Type.WAVE,
                            new File(fa));
                finalFileName = fa;
                }else {
                    AudioSystem.write(appendedFiles,
                            AudioFileFormat.Type.WAVE,
                            new File(tr));
                finalFileName = tr;
                }
                clip1.close();
                clip2.close();

                if(t) {
                    System.gc();
                    new File(tr).setWritable(true);
                    System.gc();
                    new File(tr).delete();
                    System.gc();
                }
                else
                {
                    System.gc();
                    new File(fa).setWritable(true);
                    System.gc();
                    new File(fa).delete();
                    System.gc();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            t = !t;
            firstTime = false;
        }
    }
}
