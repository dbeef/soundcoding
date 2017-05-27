package com.dbeef.soundcoding;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.dbeef.soundcoding.output.Broadcaster;
import com.dbeef.soundcoding.utils.FileMerger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

/**
 * Created by dbeef on 18.03.17.
 */
public class StarterBroadcaster implements Runnable {

    private String message;
    private boolean mergingFiles;

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

    public boolean isMergingFiles() {
        return mergingFiles;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void run() {

        mergingFiles = false;

        LinkedList<String> fileNames = new LinkedList<String>();
        String s = message;

        long startTime = System.currentTimeMillis();

        for (int a = 0; a < s.length(); a++) {

            String tempMessage = null;

            if (a > 0 && message.charAt(a) == '$') {
                tempMessage = "$" + message.charAt(a + 1);
                a++;
            }

            Broadcaster broadcaster = new Broadcaster();

            try {
                if (tempMessage == null) {
                    broadcaster.sendMessage(Integer.toString((int) s.charAt(a)), a, fileNames);
                } else {
                    System.out.println("Sending " + tempMessage);
                    broadcaster.sendMessage(tempMessage, a, fileNames);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        FileMerger fileMerger = new FileMerger();
        fileMerger.merge(fileNames);

        mergingFiles = true;

        Music music = Gdx.audio.newMusic(Gdx.files.internal("0A.wav"));
        music.setLooping(false);
        music.play();
        while(music.isPlaying()){
            try {
                Thread.sleep(500);
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
