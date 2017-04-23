package com.dbeef.soundcoding.models;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by dbeef on 31.03.17.
 */
public class GameInformation {

    Map<String, Integer> variablesAndValues;

    public GameInformation() {
        variablesAndValues = new LinkedHashMap<String, Integer>();
    }

    public Map<String, Integer> getVariablesAndValues() {
        return variablesAndValues;
    }

    public void putVariable(String variable, int value) {
        variablesAndValues.put(variable + ";" + variablesAndValues.size(), value);
    }
}