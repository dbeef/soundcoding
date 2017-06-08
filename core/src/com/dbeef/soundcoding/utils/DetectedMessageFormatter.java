package com.dbeef.soundcoding.utils;

import com.dbeef.soundcoding.models.DetectedFrequency;
import com.dbeef.soundcoding.models.GameInformationFrequencies;

import java.util.ArrayList;

/**
 * Takes String directly from message decoder (i.e 00 111 0) and returns formatted String (i.e 010).
 */
public class DetectedMessageFormatter {

    private ArrayList<String> separatedMessages;
    private String detectedMessage;
    private String formattedMessage;
    private String formattedMessageInASCII;
    private String message;
    private String experiment;

    public DetectedMessageFormatter(ArrayList<DetectedFrequency> detectedFrequencies) {

        separatedMessages = new ArrayList<String>();
        formattedMessageInASCII = "";
        formattedMessage = "";
        detectedMessage = "";

        for (DetectedFrequency f : detectedFrequencies) {
            detectedMessage += f.getMatchedSign();
        }
        if (detectedMessage.length() > 0) {
            formatMessage(detectedMessage);
        }
    }

    public String getExperiment() {
        return experiment;
    }

    private void formatSpecialMessages() {
        for (String specialVariable : GameInformationFrequencies.ALL_SPECIAL_VARIABLES)
            for (int a = 0; a < separatedMessages.size(); a++)
                if (separatedMessages.get(a).contains(specialVariable))
                    separatedMessages.set(a, specialVariable);
    }

    public ArrayList<String> getSeparatedMessages() {
        return separatedMessages;
    }

    public String getFormattedMessageInASCII() {
        return formattedMessageInASCII;
    }

    public void formatMessage(String message) {

        formattedMessage = "";
        int index = 0;
        while (index != message.length() - 1) {
            index++;
            if (message.charAt(index) != message.charAt(index - 1)) {
                if (formattedMessage.length() == 0)
                    formattedMessage += message.charAt(index - 1);
                formattedMessage += message.charAt(index);
            }
        }

        System.out.println("Experiment:");

        formattedMessage = formattedMessage.replaceAll(" ", "");
        String exp = formattedMessage.replaceAll("/", "");
        System.out.println("Exp before:");
        for (String specialVar : GameInformationFrequencies.ALL_SPECIAL_VARIABLES) {
            if (exp.contains(specialVar)) {
                if (!specialVar.equals("\\{") && !specialVar.equals("\\}") && !specialVar.equals("\"") && !specialVar.equals(":") && !specialVar.equals(",")) {
                    int position = exp.indexOf(specialVar);
                    System.out.println("Position: " + position);
                    System.out.println(exp.length());
                    exp = exp.replace(specialVar, "");
                    exp = exp.substring(0, position) + specialVar + exp.substring(position, exp.length());
                }
            }
        }


        if(exp.contains("\"variablesAndValues\":"))
        {
            int position = exp.indexOf("\"variablesAndValues\":");
            exp = exp.replace(("\"variablesAndValues\":"),"");
            exp = exp.substring(0, position) + ("\"variablesAndValues\":{") + exp.substring(position, exp.length());
        }

        experiment = exp;
        if(exp.length() > 0 && exp.charAt(exp.length()-1) == '}'){
            experiment += "}";
            System.out.println("Dodaje!");
        }

        System.out.println(exp);

    }

    public String formatMessageInASCII(String formattedMessage) {
        return formattedMessage;
    }

    public String getFormattedMessage() {
        return formattedMessage;
    }

    public void setFormattedMessage(String formattedMessage) {
        this.formattedMessage = formattedMessage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
