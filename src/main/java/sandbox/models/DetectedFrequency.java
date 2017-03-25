package sandbox.models;

import sandbox.utils.Variables;

/**
 * Model for input from {@MessageDecoder}.
 */
public class DetectedFrequency {

    private float timeOfDetection;
    private float detectedFrequency;
    private float matchedFrequency;
    private String matchedSign;

    public DetectedFrequency(float timeOfDetection, float detectedFrequency) {
        for (int a = 0; a < Variables.DECIMAL_FREQUENCIES.length; a++) {
            if (detectedFrequency > Variables.DECIMAL_FREQUENCIES[a] - Variables.TOLERANCE && detectedFrequency < Variables.DECIMAL_FREQUENCIES[a] +
                    Variables.TOLERANCE) {
                matchedFrequency = Variables.DECIMAL_FREQUENCIES[a];
                matchedSign = Integer.toString(a);
            }
        }
        if (matchedSign == null) {
            if (detectedFrequency > Variables.PAUSE_FREQUENCY - Variables.TOLERANCE && detectedFrequency < Variables.PAUSE_FREQUENCY +
                    Variables.TOLERANCE) {
                matchedFrequency = Variables.PAUSE_FREQUENCY;
                matchedSign = " ";
            } else if (detectedFrequency > Variables.START_END_FREQUENCY - Variables.TOLERANCE && detectedFrequency < Variables.START_END_FREQUENCY +
                    Variables.TOLERANCE) {
                matchedFrequency = Variables.START_END_FREQUENCY;
                matchedSign = "/";
            } else {
                matchedFrequency = detectedFrequency;
                matchedSign = "";
            }
        }
    }

    public String getMatchedSign() {
        return matchedSign;
    }

    public float getTimeOfDetection() {
        return timeOfDetection;
    }

    public float getDetectedFrequency() {
        return detectedFrequency;
    }

    public float getMatchedFrequency() {
        return matchedFrequency;
    }
}
