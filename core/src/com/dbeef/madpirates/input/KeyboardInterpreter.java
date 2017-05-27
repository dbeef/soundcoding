package com.dbeef.madpirates.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.dbeef.madpirates.camera.ImprovedCamera;
import com.dbeef.madpirates.physics.BodiesDatabase;
import com.dbeef.soundcoding.StarterBroadcaster;
import com.dbeef.soundcoding.StarterSniffer;
import com.dbeef.soundcoding.models.GameInformation;
import com.dbeef.soundcoding.models.GameInformationFrequencies;
import com.dbeef.soundcoding.utils.SoundifiedJsonDecoder;
import com.google.gson.Gson;

/**
 * Created by dbeef on 26.05.17.
 */
public class KeyboardInterpreter {

    private SoundifiedJsonDecoder decoder;
    private InputModel inputModel;
    private StarterBroadcaster starterBroadcaster;
    private Thread broadcasterThread;
    private StarterSniffer starterSniffer;
    public KeyboardInterpreter(StarterSniffer starterSniffer, SoundifiedJsonDecoder soundifiedJsonDecoder) {
        inputModel = new InputModel();
        this.starterSniffer = starterSniffer;
        decoder = soundifiedJsonDecoder;
    }

    public InputModel getInputModel() {
        return inputModel;
    }

    public StarterBroadcaster getStarterBroadcaster() {
        return starterBroadcaster;
    }

    public Thread getBroadcasterThread() {
        return broadcasterThread;
    }

    public void update(BodiesDatabase bodiesDatabase, ImprovedCamera camera, InputInterpreter iI) {

        float deltaTime = Gdx.graphics.getDeltaTime();

        if (iI.isTouched()) {
            bodiesDatabase.captureSimulation();
            System.out.println("Capturing simulation.");
        }
        if (Gdx.input.isKeyPressed(Input.Keys.K)) {
            inputModel.setSimulationTime(3);
            inputModel.setWaitingForInput(true);
            decoder.setMessage("");
            decoder.setJsonParsedSuccessfully(false);
            inputModel.setSniffing(false);
        }

            if (Gdx.input.isKeyPressed(Input.Keys.T)) {
                inputModel.setWaitingForInput(true);
                decoder.setMessage("");
                decoder.setJsonParsedSuccessfully(false);
                inputModel.setSniffing(false);
                starterSniffer.clearDetectedMessages();
        }


        if (Gdx.input.isKeyPressed(Input.Keys.Z)) {
            camera.takeZoomDelta(deltaTime);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.L)) {
            inputModel.setSniffing(!inputModel.isSniffing());
            if (inputModel.isSniffing()) {
                starterSniffer.clearDetectedMessages();
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.X)) {
            camera.takeZoomDelta(-deltaTime);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.F)) {
 //           bodiesDatabase.setShipSailingSpeed(bodiesDatabase.getShipSailingSpeed() + //deltaTime * 0.25f);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
   //         bodiesDatabase.setShipSailingSpeed(bodiesDatabase.getShipSailingSpeed() - //deltaTime * 0.25f);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
            if (broadcasterThread == null) {
                starterBroadcaster = new StarterBroadcaster();
                broadcasterThread = new Thread(starterBroadcaster);
            }
            if (!broadcasterThread.isAlive()) {
                broadcasterThread = new Thread(starterBroadcaster);
                GameInformation information = new GameInformation();

                information.putVariable(GameInformationFrequencies.PLAYER_POS_X, (int) bodiesDatabase.getBodies().get(0).getPosition().x);
                information.putVariable(GameInformationFrequencies.PLAYER_POS_Y, (int) bodiesDatabase.getBodies().get(0).getPosition().y);

                Gson gson = new Gson();
                String json = gson.toJson(information);
                GameInformationFrequencies gameInformationFrequencies = new GameInformationFrequencies();
                System.out.println(json);
                json = gameInformationFrequencies.translateJSONToSound(json);
                System.out.println(json);
                starterBroadcaster.setMessage(json);
                broadcasterThread.start();
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.P)) {
            if (inputModel.getSimulationTime() == 0)
                inputModel.setWaitingForInput(true);
            //   simulationTime = 3;
        }
    }
}
