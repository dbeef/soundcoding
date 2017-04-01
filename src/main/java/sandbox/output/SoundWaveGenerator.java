package sandbox.output;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.ugens.WavePlayer;
import sandbox.utils.Variables;

/**
 * Class which is intended to generate sound wave of specific frequency.
 */
public class SoundWaveGenerator extends Thread {

    private AudioContext audioContext;

    private WavePlayer decimalWavePlayers[];
    private WavePlayer pauseWavePlayer;
    private WavePlayer start_endWavePlayer;

    private int frequency;

    public SoundWaveGenerator(AudioContext ac) {
        audioContext = ac;
decimalWavePlayers = new WavePlayer[10];
        for(int a =0;a<10;a++)
            decimalWavePlayers[a] =  new WavePlayer(audioContext, Variables.DECIMAL_FREQUENCIES[a], Buffer.SINE);
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
        for(int a =0;a<10;a++)
            if(frequency == (Variables.DECIMAL_FREQUENCIES[a]))
                audioContext.out.addInput(decimalWavePlayers[a]);
        if(frequency == (Variables.PAUSE_FREQUENCY))
        audioContext.out.addInput(pauseWavePlayer);
        if(frequency == (Variables.START_END_FREQUENCY))
            audioContext.out.addInput(start_endWavePlayer);

        synchronized(audioContext) {
            audioContext.start();
        }
    }

    public void stopAudioContext() {
        audioContext.out.clearInputConnections();
   }
}