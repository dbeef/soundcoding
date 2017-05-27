package com.dbeef.soundcoding;

import com.dbeef.soundcoding.input.Sniffer;
import com.dbeef.soundcoding.models.DetectedFrequency;
import com.dbeef.soundcoding.utils.DetectedMessageFormatter;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.util.ArrayList;

/**
 * Created by dbeef on 18.03.17.
 */
public class StarterSniffer implements Runnable {
    private boolean sniffing;
    private String message;
    private Sniffer sniffer;
    public void setSniffing(boolean sniffing) {
        this.sniffing = sniffing;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public void run() {
         sniffer = null;
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
            synchronized (sniffer.getDetectedFrequencies()) {

                DetectedMessageFormatter detectedMessageFormatter = new DetectedMessageFormatter((ArrayList<DetectedFrequency>) sniffer.getDetectedFrequencies().clone());

                System.out.println("Formatted: " + detectedMessageFormatter.getFormattedMessage());
                //          sniffer.getDetectedFrequencies().clear();
                System.out.println("Separated messages:");
                message = "";
    //            if (!sniffing) {
      //              detectedMessageFormatter.setFormattedMessage("");
        //            detectedMessageFormatter.setMessage("");
          //      }
                //TODO
                //Połączyć to z broadcasterem
                for (String s : detectedMessageFormatter.getSeparatedMessages()) {
                    System.out.println(s);
                    message += s;
                }
            }
        }
    }
    public void clearDetectedMessages(){
        sniffer.getDetectedFrequencies().clear();
    }
}