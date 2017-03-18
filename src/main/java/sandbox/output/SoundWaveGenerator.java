package sandbox.output;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.ugens.WavePlayer;
import sandbox.utils.Variables;

/**
 * Class which is intended to generate sound wave of specific frequency.
 */
public class SoundWaveGenerator implements Runnable {

    private AudioContext audioContext;

    private WavePlayer zeroWavePlayer;
    private WavePlayer oneWavePlayer;
    private WavePlayer pauseWavePlayer;
    private WavePlayer start_endWavePlayer;

    private int frequency;

    public SoundWaveGenerator() {
        audioContext = new AudioContext();

        zeroWavePlayer = new WavePlayer(audioContext, Variables.ZERO_FREQUENCY, Buffer.SINE);
        oneWavePlayer = new WavePlayer(audioContext, Variables.ONE_FREQUENCY, Buffer.SINE);
        pauseWavePlayer = new WavePlayer(audioContext, Variables.PAUSE_FREQUENCY, Buffer.SINE);
        start_endWavePlayer = new WavePlayer(audioContext, Variables.START_END_FREQUENCY, Buffer.SINE);

    }

    public AudioContext getAudioContext() {
        return audioContext;
    }

    public void setAudioContext(AudioContext audioContext) {
        this.audioContext = audioContext;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    @Override
    public void run() {
        if(frequency == (Variables.PAUSE_FREQUENCY))
        audioContext.out.addInput(pauseWavePlayer);
        if(frequency ==(Variables.ONE_FREQUENCY))
            audioContext.out.addInput(oneWavePlayer);
        if(frequency == (Variables.ZERO_FREQUENCY))
            audioContext.out.addInput(zeroWavePlayer);
        if(frequency == (Variables.START_END_FREQUENCY))
            audioContext.out.addInput(start_endWavePlayer);

        audioContext.start();
    }

    public void stop() {
        audioContext.stop();
        audioContext.out.clearInputConnections();
    }
}

