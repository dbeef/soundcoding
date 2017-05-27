package com.dbeef.madpirates.input;

/**
 * Created by dbeef on 27.05.17.
 */
public class InputModel {
    private boolean sniffing;
    private boolean waitingForInput;
    private float simulationTime;

    public boolean isSniffing() {
        return sniffing;
    }

    public void setSniffing(boolean sniffing) {
        this.sniffing = sniffing;
    }

    public boolean isWaitingForInput() {
        return waitingForInput;
    }

    public void setWaitingForInput(boolean waitingForInput) {
        this.waitingForInput = waitingForInput;
    }

    public float getSimulationTime() {
        return simulationTime;
    }

    public void setSimulationTime(float simulationTime) {
        this.simulationTime = simulationTime;
    }
}
