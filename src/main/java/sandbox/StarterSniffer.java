package sandbox;

import sandbox.input.Sniffer;
import sandbox.utils.DetectedMessageFormatter;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Created by dbeef on 18.03.17.
 */
public class StarterSniffer implements Runnable {
    @Override
    public void run() {
        Sniffer sniffer = null;
        try {
            sniffer = new Sniffer();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            DetectedMessageFormatter detectedMessageFormatter = new DetectedMessageFormatter(sniffer.getDetectedFrequencies());
            System.out.println("Formatted: " + detectedMessageFormatter.getFormattedMessage());
  //          sniffer.getDetectedFrequencies().clear();
     System.out.println("Separated messages:");
for(String s : detectedMessageFormatter.getSeparatedMessages()) {
    System.out.println(s);
}
        }
    }
}