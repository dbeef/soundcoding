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

    public static final int[] FREQUENCIES = {3950,4000,4050,4100,4150,4200,4300,4250};
    public static final String[] VALUES = {ARRAY_NAME, PLAYER_POS_X, PLAYER_POS_Y, HITPOINTS,EQUIPMENT,APPLE,GOLD,SWORD};

    Map<String, Integer> frequenciesForValues;

    public GameInformationFrequencies() {
        frequenciesForValues = new LinkedHashMap<String, Integer>();
        frequenciesForValues.put(ARRAY_NAME,4100);
        frequenciesForValues.put(PLAYER_POS_X, 4200);
        frequenciesForValues.put(PLAYER_POS_Y, 4300);
        frequenciesForValues.put(HITPOINTS, 4400);
        frequenciesForValues.put(EQUIPMENT, 4500);
        frequenciesForValues.put(APPLE, 4600);
        frequenciesForValues.put(GOLD, 4700);
        frequenciesForValues.put(SWORD, 4800);
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
       /* for (Map.Entry<String, Integer> entry : frequenciesForValues.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            if (json.contains(key)) {
                json = json.replace(key, "$" + value + "$");
            //    json = json.replace(key, "$");
            }
        }
        */
       int a =0;
       for(String value : VALUES){
           if(json.contains(value)){
     json =  json.replace(value,"$" + Integer.toString(a));
           }
           a++;
       }
        System.out.println("Sound-formatted string is " + json);

        return json;
    }
    public void translateSoundToJSON(){
        // TODO: 01.04.17  
    }
}