package com.dbeef.soundcoding.utils;

import java.io.IOException;

import com.badlogic.gdx.Audio;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.Bead;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.data.audiofile.AudioFileType;
import net.beadsproject.beads.events.KillTrigger;
import net.beadsproject.beads.ugens.Clock;
import net.beadsproject.beads.ugens.Envelope;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.RecordToSample;
import net.beadsproject.beads.ugens.WavePlayer;

/**
 * https://github.com/orsjb/beads/blob/master/packages/Beads/beads_tutorial/Lesson09_RecordToSample.java
 * https://stackoverflow.com/questions/653861/join-two-wav-files-from-java
 */
public class FileRecorder {
    public void record(final AudioContext a,    final String fileIndex, final int frequency) {

        final AudioContext ac = new AudioContext();
        final Sample outputSample = new Sample(5000D);
        final RecordToSample recordToSample = new RecordToSample(ac,
                outputSample, RecordToSample.Mode.INFINITE);

        Clock clock = new Clock(ac, 700);
        clock.addMessageListener(
                new Bead() {
                    public void messageReceived(Bead message) {
                        Clock c = (Clock) message;
                            if (c.isBeat()) {
                            WavePlayer wp = new WavePlayer(ac, frequency, Buffer.SINE);
                            wp.setTimerMode(true);
                            ac.out.addInput(wp);
                        }
                        if (c.getCount() >= 4) {
                            recordToSample.clip();
                            Sample sample = recordToSample.getSample();
                            try {
                                System.out.println("file name " + fileIndex + ".wav");
                                sample.write(fileIndex + ".wav",
                                        AudioFileType.WAV);
                            } catch (IOException e) {
                                System.out.println("Couldn't save sonification:");
                                e.printStackTrace();
                            }
                            ac.stop();
                        }
                    }
                });

        recordToSample.addInput(ac.out);
        ac.out.addDependent(recordToSample);
        ac.out.addDependent(clock);

     //   ac.start();
        ac.runNonRealTime();
    }
}
