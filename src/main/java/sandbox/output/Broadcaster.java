package sandbox.output;

import sandbox.utils.Variables;

/**
 * Created by dbeef on 18.03.17.
 */
public class Broadcaster {

    static SoundWaveGenerator soundWaveGenerator;

    public void sendMessage(String message) {

        soundWaveGenerator = new SoundWaveGenerator();

        /*
        Modulates 'pause' frequency for 10 times MessageInterval seconds, without this chunk of code,
        first character of message to send would be omitted (for an unknown reason,
        my suggestion is that calling an audio interface for the first time takes too much time).
        */

        long startTime = System.currentTimeMillis();

        Thread t;
        soundWaveGenerator.setFrequency(Variables.START_END_FREQUENCY);
        t = new Thread(soundWaveGenerator);
        t.run();

        try {
            Thread.sleep(Variables.MESSAGE_INTERVAL * 3);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        soundWaveGenerator.stop();

        try {
            Thread.sleep(Variables.MESSAGE_INTERVAL * 3);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        //  Here we send every character separated by the 'pause' frequency.
        // I've created SoundWaveGenerator class because I didn't see any
        // method to generate a soundwave for a particular amount of time, so I run it in
        // a different thread and count time here.

        for (int a = 0; a < message.length(); a++) {

            for (int b = 0; b < Variables.DECIMAL_FREQUENCIES.length; b++) {
                if (Integer.parseInt(Character.toString(message.charAt(a))) == b) {
                    soundWaveGenerator.setFrequency(Variables.DECIMAL_FREQUENCIES[b]);
                System.out.println("Now " + Variables.DECIMAL_FREQUENCIES[b]);
                }
            }

            t.run();
            while (!soundWaveGenerator.getAudioContext().isRunning()) {
                try {
                    Thread.sleep(15);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
            waitFor(Variables.MESSAGE_INTERVAL);
            soundWaveGenerator.stop();

            while (soundWaveGenerator.getAudioContext().isRunning()) {
                try {
                    Thread.sleep(15);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
            waitFor(Variables.MESSAGE_INTERVAL);

            soundWaveGenerator.setFrequency(Variables.PAUSE_FREQUENCY);
            t.run();
            while (!soundWaveGenerator.getAudioContext().isRunning()) {
                try {
                    Thread.sleep(15);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
            waitFor(Variables.MESSAGE_INTERVAL);
            soundWaveGenerator.stop();

            while (soundWaveGenerator.getAudioContext().isRunning()) {
                try {
                    Thread.sleep(15);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
            waitFor(Variables.MESSAGE_INTERVAL);
        }

        soundWaveGenerator.setFrequency(Variables.START_END_FREQUENCY);
        t.run();
        waitFor(Variables.MESSAGE_INTERVAL * 3);
        soundWaveGenerator.stop();
        t.stop();
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
