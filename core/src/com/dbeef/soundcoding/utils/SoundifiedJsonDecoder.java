package com.dbeef.soundcoding.utils;

import com.dbeef.soundcoding.models.GameInformation;
import com.google.gson.Gson;

/**
 * Created by dbeef on 26.05.17.
 */
public class SoundifiedJsonDecoder implements Runnable {

    private boolean jsonParsedSuccessfully;
    private String message;
    private GameInformation gameInformation;

    public SoundifiedJsonDecoder(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isJsonParsedSuccessfully() {
        return jsonParsedSuccessfully;
    }

    public void setJsonParsedSuccessfully(boolean jsonParsedSuccessfully) {
        this.jsonParsedSuccessfully = jsonParsedSuccessfully;
    }

    public GameInformation getGameInformation() {
        return gameInformation;
    }

    public void setGameInformation(GameInformation gameInformation) {
        this.gameInformation = gameInformation;
    }

    @Override
    public void run() {
        if (message != null && !message.equals("")) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Gson gson = new Gson();
            gameInformation = gson.fromJson(message, GameInformation.class);
            jsonParsedSuccessfully = true;
        }
    }
}