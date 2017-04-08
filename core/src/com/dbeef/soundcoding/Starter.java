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

/*
        GameInformation gameInformation = new GameInformation();
        gameInformation.putVariable(GameInformationFrequencies.APPLE,3);

        Gson gson = new Gson();
        String json = gson.toJson(gameInformation,GameInformation.class);
        System.out.println("Ordinary json:");
        System.out.println(json);
        System.out.println("Converted to sound");
        System.out.println(new GameInformationFrequencies().translateJSONToSound(json));

        CRC32 checksumGenerator = new CRC32();
        checksumGenerator.update(json.getBytes());
        System.out.println("Checksum of the json:");
        System.out.println(Integer.toBinaryString((int)checksumGenerator.getValue()));
*/
            }
}
