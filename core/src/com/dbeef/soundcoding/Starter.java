package com.dbeef.soundcoding;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

/**
 * Class which is intended to take one character from user's input and send it in binary through sound.
 */
public class Starter {
    public static void main(String[] args) throws IOException, LineUnavailableException, UnsupportedAudioFileException {

        StarterBroadcaster starterBroadcaster = new StarterBroadcaster();
        StarterSniffer starterSniffer = new StarterSniffer();

        Thread broadcasterThread = new Thread(starterBroadcaster);
        Thread snifferThread = new Thread(starterSniffer);

        snifferThread.start();
        broadcasterThread.start();
    }
}
