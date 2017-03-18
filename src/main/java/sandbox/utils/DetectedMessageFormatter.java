package sandbox.utils;

import sandbox.models.DetectedFrequency;

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
            //    formatMessageInASCII();
        }
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
        formattedMessage = formattedMessage.replaceAll(" ", "");

        int a = 0;
        int index_start = 0;
        int index_end = 0;
        String temp = formattedMessage;
        String separatedMessage = "";
        while (temp.length() > 0) {
            temp = temp.replaceFirst("/", "");
            if (temp.contains("/")) {
                separatedMessage = temp.substring(0, temp.indexOf("/"));
                temp = temp.replaceFirst(separatedMessage, "");
                separatedMessages.add(formatMessageInASCII(separatedMessage));
            }
       else
           break;
        }
    }

    public String formatMessageInASCII(String formattedMessage) {
        if (!formattedMessage.equals("")) {

            int code = Integer.parseInt(formattedMessage, 2);
            char p = (char) code;
            formattedMessageInASCII = Character.toString(p);
            return formattedMessageInASCII;
        } else
            formattedMessageInASCII = "No message detected.";

        return formattedMessageInASCII;
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
