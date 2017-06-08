package com.dbeef.soundcoding.output;

import com.dbeef.soundcoding.models.GameInformationFrequencies;
import com.dbeef.soundcoding.utils.FileRecorder;
import com.dbeef.soundcoding.utils.Variables;
import net.beadsproject.beads.core.AudioContext;

import java.util.LinkedHashSet;
import java.util.LinkedList;

/**
 * Created by dbeef on 18.03.17.
 */
public class Broadcaster {

    private LinkedList<String> fileNames;
    private static AudioContext audioContext;

    public void sendMessage(String message, int index, LinkedList<String> fileNames) throws InterruptedException {
        System.out.println("in broadcaster. Message is " + message);

        System.out.println("Pause frequency is " + Variables.PAUSE_FREQUENCY);
        System.out.println("Start-end frequency is " + Variables.START_END_FREQUENCY);

        final AudioContext ac = new AudioContext();

        long startTime = System.currentTimeMillis();

  FileRecorder fileRecorder = new FileRecorder();

  fileRecorder.record(ac, index + "A", Variables.START_END_FREQUENCY);

  fileNames.add(index + "A");

  int freq = 0;

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

                    freq = ((GameInformationFrequencies.FREQUENCIES[Integer.parseInt(Character.toString(message.charAt(a)))]));

                    System.out.println("I see special variable here. Setting: " + (GameInformationFrequencies.FREQUENCIES[Integer.parseInt(Character.toString(message.charAt(a)))]));

                } else if(!message.contains("$"))
           for (int b = 0; b < Variables.DECIMAL_FREQUENCIES.length; b++) {
                        if (Character.toString(message.charAt(a)).equals(Integer.toString(b))) {
                            freq = (Variables.DECIMAL_FREQUENCIES[b]);
                            System.out.println("Now " + Variables.DECIMAL_FREQUENCIES[b]);
                        }
                    }

                fileRecorder.record(ac, index + "A" + a, freq);
                fileNames.add(index + "A" + a);

                while(ac.isRunning()){
                    Thread.sleep((50));
                }
                fileRecorder.record(ac,index + "B" + a, Variables.PAUSE_FREQUENCY);
                fileNames.add(index + "B" + a);

                while(ac.isRunning()){
                    Thread.sleep((50));
                }
            }

        fileRecorder.record(ac,index + "C", Variables.START_END_FREQUENCY);
            fileNames.add(index + "C");

        while(ac.isRunning()){
            Thread.sleep((50));
        }
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
