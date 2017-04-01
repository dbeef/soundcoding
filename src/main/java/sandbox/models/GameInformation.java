package sandbox.models;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by dbeef on 31.03.17.
 */
public class GameInformation {

    Map<String, Integer> variablesAndValues;

    public GameInformation(){
        variablesAndValues = new LinkedHashMap<String, Integer>();
    }
    public void putVariable(String variable, int value){
        variablesAndValues.put(variable,value);
    }
}