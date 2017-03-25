package sandbox;

import sandbox.output.Broadcaster;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by dbeef on 18.03.17.
 */
public class StarterBroadcaster implements Runnable {
    private static int takeInput() throws IOException {

        String input = readString();
        int character = input.charAt(0);

        System.out.println("First character of string you typed in binary:");
        System.out.println(Integer.toBinaryString(character));
        System.out.println("First character of string you typed in hex:");
        System.out.println(Integer.toHexString(character));
        System.out.println("First character of string you typed in decimal:");
        System.out.println(Integer.toString(character));

        System.out.println("Now sending your message.");

        return character;
    }

    static String readString() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter string to broadcast:");
        String s = br.readLine();
        return s;
    }

    @Override
    public void run() {
        while (true) {
            String s = null;
            try {
                s = readString();
            } catch (IOException e) {
                e.printStackTrace();
            }

            long startTime = System.currentTimeMillis();

            for (int a = 0; a < s.length(); a++) {
                System.out.println("Sending " + s.charAt(a));
                Broadcaster broadcaster = new Broadcaster();
                broadcaster.sendMessage(Integer.toString((int) s.charAt(a)));
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Done.");
            long endTime = System.currentTimeMillis();
            long totalTime = endTime - startTime;
            System.out.println("Total time of sending whole message in (ms): " + totalTime);
        }
    }
}