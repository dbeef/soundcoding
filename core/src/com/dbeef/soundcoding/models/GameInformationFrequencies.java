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
    public static final String LEFT_BRACKET = ",";
    public static final String RIGHT_BRACKET = "}";
    public static final String COLON = ":";
    public static final String QUOTE = "\"";
    public static final String COMMA = "{";
    public static final int[] FREQUENCIES = {750, 800, 850, 900, 950, 1000, 1050, 1100};
    public static String ALL_SPECIAL_VARIABLES[] = {ARRAY_NAME, PLAYER_POS_X, PLAYER_POS_Y, LEFT_BRACKET, RIGHT_BRACKET, COLON, QUOTE, COMMA};
    public Map<String, Integer> frequenciesForValues;

    public GameInformationFrequencies() {
        frequenciesForValues = new LinkedHashMap<String, Integer>();
        frequenciesForValues.put(ALL_SPECIAL_VARIABLES[0], FREQUENCIES[0]);
        frequenciesForValues.put(ALL_SPECIAL_VARIABLES[1], FREQUENCIES[1]);
        frequenciesForValues.put(ALL_SPECIAL_VARIABLES[2], FREQUENCIES[2]);
        frequenciesForValues.put(ALL_SPECIAL_VARIABLES[3], FREQUENCIES[3]);
        frequenciesForValues.put(ALL_SPECIAL_VARIABLES[4], FREQUENCIES[4]);
        frequenciesForValues.put(ALL_SPECIAL_VARIABLES[5], FREQUENCIES[5]);
        frequenciesForValues.put(ALL_SPECIAL_VARIABLES[6], FREQUENCIES[6]);
        frequenciesForValues.put(ALL_SPECIAL_VARIABLES[7], FREQUENCIES[7]);
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
        int a = 0;
        for (String value : ALL_SPECIAL_VARIABLES) {
            if (json.contains(value)) {
                json = json.replace(value, "$" + Integer.toString(a));
            }
            a++;
        }
        System.out.println("Sound-formatted string is " + json);
        return json;
    }

    public void translateSoundToJSON() {
        // TODO: 01.04.17  
    }
}