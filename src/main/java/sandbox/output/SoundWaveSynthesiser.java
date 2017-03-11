package sandbox.output;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.ugens.WavePlayer;

/**
 * Class which is intended to generate sound wave of specific frequency.
 */
public class SoundWaveSynthesiser implements Runnable {

    private AudioContext audioContext;
    private WavePlayer wavePlayer;
    private float frequency;

    public SoundWaveSynthesiser() {
        audioContext = new AudioContext();
    }

    public AudioContext getAudioContext() {
        return audioContext;
    }

    public void setAudioContext(AudioContext audioContext) {
        this.audioContext = audioContext;
    }

    public float getFrequency() {
        return frequency;
    }

    public void setFrequency(float frequency) {
        this.frequency = frequency;
    }

    @Override
    public void run() {
        wavePlayer = new WavePlayer(audioContext, frequency, Buffer.SINE);
        audioContext.out.addInput(wavePlayer);
        audioContext.start();
    }

    public void stop() {
        //Not sure if I really need to call all of these methods
        //I Will change it when I make more research.
        audioContext.stop();
        audioContext.out.clearInputConnections();
        audioContext.out.clearDependents();
    }
}

