package com.dbeef.soundcoding.models;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by dbeef on 01.04.17.
 */
public class GameInformationFrequencies {

    public static final String ARRAY_NAME = "variablesAndValues";
    public static final String PLAYER_POS_X = "PLAYER_POS_X";
    public static final String PLAYER_POS_Y = "PLAYER_POS_Y";
    public static final String HITPOINTS = "HITPOINTS";
    public static final String EQUIPMENT = "EQUIPMENT";
    public static final String APPLE = "APPLE";
    public static final String GOLD = "GOLD";
    public static final String SWORD = "SWORD";

    Map<String, Integer> frequenciesForValues;

    public GameInformationFrequencies() {
        frequenciesForValues = new LinkedHashMap<String, Integer>();
        frequenciesForValues.put(ARRAY_NAME,3950);
        frequenciesForValues.put(PLAYER_POS_X, 4000);
        frequenciesForValues.put(PLAYER_POS_Y, 4050);
        frequenciesForValues.put(HITPOINTS, 4100);
        frequenciesForValues.put(EQUIPMENT, 4150);
        frequenciesForValues.put(APPLE, 4200);
        frequenciesForValues.put(GOLD, 4300);
        frequenciesForValues.put(SWORD, 4250);
    }

    public int getFrequencyForString(String s) {
        for (Map.Entry<String, Integer> entry : frequenciesForValues.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            if (s.equals(key))
                return value;
        }
        return -1;
    }

    public String getStringForFrequency(int frequency) {
        for (Map.Entry<String, Integer> entry : frequenciesForValues.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            if (frequency == value)
                return key;
        }
        return null;
    }

    public String translateJSONToSound(String json) {
        for (Map.Entry<String, Integer> entry : frequenciesForValues.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            if (json.contains(key)) {
                json = json.replace(key, "$" + value + "$");
            }
        }
        return json;
    }
    public void translateSoundToJSON(){
        // TODO: 01.04.17  
    }
}