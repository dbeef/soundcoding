package com.dbeef.soundcoding.output;

import com.dbeef.soundcoding.models.GameInformation;
import com.dbeef.soundcoding.models.GameInformationFrequencies;
import com.dbeef.soundcoding.utils.Variables;
import com.google.gson.Gson;
import net.beadsproject.beads.core.AudioContext;

/**
 * Created by dbeef on 18.03.17.
 */
public class Broadcaster {

    static SoundWaveGenerator soundWaveGenerator;
    static AudioContext audioContext;

    public void sendMessage(String message) throws InterruptedException {
        System.out.println("in broadcaster. Message is " + message);

            audioContext = new AudioContext();
            soundWaveGenerator = new SoundWaveGenerator(audioContext);

        /*
        Modulates 'pause' frequency for 10 times MessageInterval seconds, without this chunk of code,
        first character of message to send would be omitted (for an unknown reason,
        my suggestion is that calling an audio interface for the first time takes too much time).
        */

        long startTime = System.currentTimeMillis();

        soundWaveGenerator.setFrequency(Variables.START_END_FREQUENCY);
        soundWaveGenerator.start();

        waitFor(Variables.MESSAGE_INTERVAL * 3);

        soundWaveGenerator.stopAudioContext();
        soundWaveGenerator.join();

        soundWaveGenerator = new SoundWaveGenerator(audioContext);

        // Here we send every character separated by the 'pause' frequency.
        // I've created SoundWaveGenerator class because I didn't see any
        // method to generate a soundwave for a particular amount of time, so I run it in
        // a different thread and count time here.
            for (int a = 0; a < message.length(); a++) {
            if(message.charAt(a) == '$')
                a++;

                System.out.println("this message is " + message);
                System.out.println("current char is " + Character.toString(message.charAt(a)));

                if (a > 0 && message.charAt(a - 1) == '$') {

                    soundWaveGenerator.setFrequency((GameInformationFrequencies.FREQUENCIES[Integer.parseInt(Character.toString(message.charAt(a)))]));

                    System.out.println("I see special variable here. Setting: " + (GameInformationFrequencies.FREQUENCIES[Integer.parseInt(Character.toString(message.charAt(a)))]));

                } else if(!message.contains("$"))
           for (int b = 0; b < Variables.DECIMAL_FREQUENCIES.length; b++) {
                        if (Character.toString(message.charAt(a)).equals(Integer.toString(b))) {
                            soundWaveGenerator.setFrequency(Variables.DECIMAL_FREQUENCIES[b]);
                            System.out.println("Now " + Variables.DECIMAL_FREQUENCIES[b]);
                        }
                    }

                soundWaveGenerator.start();

                waitFor(Variables.MESSAGE_INTERVAL);

                soundWaveGenerator.stopAudioContext();
                soundWaveGenerator.join();

                soundWaveGenerator = new SoundWaveGenerator(audioContext);

                soundWaveGenerator.setFrequency(Variables.PAUSE_FREQUENCY);
                soundWaveGenerator.start();

                waitFor(Variables.MESSAGE_INTERVAL);

                soundWaveGenerator.stopAudioContext();
                soundWaveGenerator.join();

                soundWaveGenerator = new SoundWaveGenerator(audioContext);
            }

            soundWaveGenerator.setFrequency(Variables.START_END_FREQUENCY);
            soundWaveGenerator.start();

            waitFor(Variables.MESSAGE_INTERVAL * 3);

            soundWaveGenerator.stopAudioContext();
            soundWaveGenerator.join();

            long endTime = System.currentTimeMillis();
            long totalTime = endTime - startTime;
            System.out.println("Total time of sending the message in (ms): " + totalTime);

        }

    private void waitFor(int miliseconds) {
        try {
            Thread.sleep(miliseconds);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}
