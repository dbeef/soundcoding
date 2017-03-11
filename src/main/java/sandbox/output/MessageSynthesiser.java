package sandbox.output;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Class which is intended to take one character from user's input and send it in binary through sound.
 */
public class MessageSynthesiser {

    private static final int PAUSE_FREQUENCY = 3000;
    private static final int ZERO_FREQUENCY = 2500;
    private static final int ONE_FREQUENCY = 2000;
    private static final int MESSAGE_INTERVAL = 100;

    static SoundWaveSynthesiser synthesiser;

    public static void main(String[] args) throws IOException {

        synthesiser = new SoundWaveSynthesiser();

        String input = readString();
        int character = input.charAt(0);

        System.out.println("First character of string you typed in binary:");
        System.out.println(Integer.toBinaryString(character));
        System.out.println("First character of string you typed in hex:");
        System.out.println(Integer.toHexString(character));

        System.out.println("Now sending your message.");

        MessageSynthesiser messageSynthesiser = new MessageSynthesiser();
        messageSynthesiser.sendMessage(Integer.toBinaryString(character));

        System.out.println("Done.");
        System.exit(0);
    }

    static String readString() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter one character:");
        String s = br.readLine();
        return s;
    }

    public void sendMessage(String message) {

        /*
        Modulates 'pause' frequency for 3 seconds, without this chunk of code,
        first character of message to send would be omitted (for an unknown reason,
        my suggestion is that calling an audio interface for the first time takes too much time).
        */

        Thread t;
        synthesiser.setFrequency(PAUSE_FREQUENCY);
        t = new Thread(synthesiser);
        t.run();

        try {
            Thread.sleep(MESSAGE_INTERVAL * 10);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        synthesiser.stop();

        try {
            Thread.sleep(MESSAGE_INTERVAL * 10);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        //  Here we send every character separated by the 'pause' frequency.
        // I've created SoundWaveSynthesiser class because I didn't see any
        // method to generate a soundwave for a particular time, so I run it in
        // a different thread and count time here.
        for (int a = 0; a < message.length(); a++) {

            if (message.charAt(a) == '0')
                synthesiser.setFrequency(ZERO_FREQUENCY);
            else
                synthesiser.setFrequency(ONE_FREQUENCY);

            t = new Thread(synthesiser);
            t.run();
            waitFor(MESSAGE_INTERVAL);
            synthesiser.stop();

            //If I generate soundwaves one by one, they merge very badly
            //I am not sure if I do it wrong or it's some BeadsProject issue
            waitFor(MESSAGE_INTERVAL);

            synthesiser.setFrequency(PAUSE_FREQUENCY);
            t.run();
            waitFor(MESSAGE_INTERVAL);
            synthesiser.stop();

            waitFor(MESSAGE_INTERVAL);
        }
    }

    private void waitFor(int miliseconds) {
        try {
            Thread.sleep(miliseconds);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}
