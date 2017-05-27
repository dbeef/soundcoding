package com.dbeef.madpirates.hud;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.dbeef.madpirates.auxiliary.HudMode;
import com.dbeef.madpirates.input.InputModel;
import com.dbeef.soundcoding.StarterBroadcaster;
import com.dbeef.soundcoding.StarterSniffer;
import com.dbeef.soundcoding.input.Sniffer;
import com.dbeef.soundcoding.models.GameInformation;
import com.dbeef.soundcoding.models.GameInformationFrequencies;
import com.dbeef.soundcoding.output.Broadcaster;
import com.dbeef.soundcoding.utils.SoundifiedJsonDecoder;

import java.awt.*;

/**
 * Created by dbeef on 26.05.17.
 */
public class HUD {
    public static void draw(Batch batch, InputModel inputModel , BitmapFont font, StarterBroadcaster starterBroadcaster, Thread broadcasterThread, SoundifiedJsonDecoder decoder, StarterSniffer starterSniffer) {

        float simulationTime = inputModel.getSimulationTime();
        boolean waitingForInput = inputModel.isWaitingForInput();
        boolean sniffing = inputModel.isSniffing();

        HudMode hudMode;
        if (simulationTime == 0 && waitingForInput != true) {
            hudMode = HudMode.PLANNING;
            font.draw(batch, "Tura planowania. P by zakonczyc ture.", -390, 235);
        }
        if (simulationTime != 0 && waitingForInput != true) {
            hudMode = HudMode.SIMULATING;
            font.draw(batch, "Tura symulacji.", -390, 235);
        }
        if (waitingForInput && (broadcasterThread == null || !broadcasterThread.isAlive()) && (decoder == null || !decoder.isJsonParsedSuccessfully()) && !sniffing) {
            hudMode = HudMode.AFTER_PLANNING;
            font.draw(batch, "Oczekiwanie na informacje od drugiego gracza", -390, 235);
            font.draw(batch, "Wcisnij G aby rozeslac informacje", -390, -220);
            font.draw(batch, "Wcisnij L aby nasluchiwac informacje", 100, -220);
            font.draw(batch, "Nasluchiwanie informacji: " + sniffing, 150, 235);
        }

        if(broadcasterThread != null && broadcasterThread.isAlive() && starterBroadcaster.isMergingFiles() && (decoder == null || !decoder.isJsonParsedSuccessfully())){
            hudMode = HudMode.BROADCASTING_EMITTING;
            font.draw(batch, "Wlasnie nadaje wiadomosc.", -390, 235);
        }
        if(broadcasterThread != null && broadcasterThread.isAlive() && !starterBroadcaster.isMergingFiles()){
            hudMode = HudMode.BROADCASTING_EMITTING;
            font.draw(batch, "Generowanie wiadomosci... ", -390, 235);
        }
        if(decoder != null) {
            if (decoder.getMessage() != null && !decoder.getMessage().equals("") && !decoder.isJsonParsedSuccessfully() && sniffing) {
                hudMode = HudMode.SNIFFING;
                font.draw(batch, "Odczytuje wiadomosc. Wcisnij T aby wyczyscic tablice.", -390, 220);
                font.draw(batch, "Dotychczas odczytano: " + starterSniffer.getMessage(), -390, -220);
            }
            if(decoder.isJsonParsedSuccessfully()){
                hudMode = HudMode.SNIFFING_DONE;
                font.draw(batch, "Wcisnij K aby zatwierdzic. T aby wrocic. ", 75, 235);
                font.draw(batch, "Pomyślnie odebrano wiadomosc!.", -390, 235);
                font.draw(batch, "Pozycja x to: " + decoder.getGameInformation().getVariablesAndValues().get(GameInformationFrequencies.PLAYER_POS_X) + " pozycja y to: " + decoder.getGameInformation().getVariablesAndValues().get(GameInformationFrequencies.PLAYER_POS_Y), -390, -220);

                System.out.println(decoder.getGameInformation().getVariablesAndValues().size());

            }
        }
    }
}