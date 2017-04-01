package sandbox;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Glide;
import net.beadsproject.beads.ugens.WavePlayer;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.zip.CRC32;

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

        //
        CRC32 checksumGenerator = new CRC32();
        checksumGenerator.update(new byte[302]);
                System.out.println(Integer.toBinaryString((int)checksumGenerator.getValue()));

        }
}
