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
        if (detectedFrequency > 2950 && detectedFrequency < 3050) {
            matchedFrequency = Variables.PAUSE_FREQUENCY;
            matchedSign = " ";
        } else if (detectedFrequency > 2450 && detectedFrequency < 2550) {
            matchedFrequency = Variables.ZERO_FREQUENCY;
            matchedSign = "0";
        } else if (detectedFrequency > 1950 && detectedFrequency < 2050) {
            matchedFrequency = Variables.ONE_FREQUENCY;
            matchedSign = "1";
        }
        else if (detectedFrequency > 3450 && detectedFrequency < 3550) {
            matchedFrequency = Variables.START_END_FREQUENCY;
            matchedSign = "/";
        }
        else
        {
            matchedFrequency = detectedFrequency;
            matchedSign = "";
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
