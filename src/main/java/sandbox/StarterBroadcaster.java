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

        System.out.println("Now sending your message.");

        return character;
    }

    static String readString() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter one character:");
        String s = br.readLine();
        return s;
    }

    @Override
    public void run() {
        while (true) {
            Broadcaster broadcaster = new Broadcaster();

            try {
                broadcaster.sendMessage(Integer.toBinaryString(
                        takeInput()));
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Done.");
        }
    }

}
