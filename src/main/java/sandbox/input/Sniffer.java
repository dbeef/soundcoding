package sandbox.input;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.io.jvm.JVMAudioInputStream;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import sandbox.models.DetectedFrequency;

import javax.sound.sampled.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Class mostly taken from TarsosDSP example.
 * Starts sniffing all frequences when object created
 * and adds them to the DetectedFrequency array for further managing.
 */
public class Sniffer implements PitchDetectionHandler {

    private static final long serialVersionUID = 3501426880288136245L;
    private ArrayList<DetectedFrequency> detectedFrequencies = new ArrayList<DetectedFrequency>();
    private AudioDispatcher dispatcher;
    private Mixer currentMixer;
    private PitchProcessor.PitchEstimationAlgorithm algo;

    private ActionListener algoChangeListener = new ActionListener() {
        @Override
        public void actionPerformed(final ActionEvent e) {
            String name = e.getActionCommand();
            PitchProcessor.PitchEstimationAlgorithm newAlgo = PitchProcessor.PitchEstimationAlgorithm.valueOf(name);
            algo = newAlgo;
            try {
                setNewMixer(currentMixer);
            } catch (LineUnavailableException e1) {
                e1.printStackTrace();
            } catch (UnsupportedAudioFileException e1) {
                e1.printStackTrace();
            }
        }
    };

    public Sniffer() throws LineUnavailableException, UnsupportedAudioFileException {

        //TODO Make output switching more elegant way.

        algo = PitchProcessor.PitchEstimationAlgorithm.YIN;

        int a = 0;
        for (Mixer.Info info : SnifferUtils.getMixerInfo(false, true)) {
            a++;
            if (a == 2) {
                Mixer newValue = AudioSystem.getMixer(info);
                setNewMixer(newValue);
                System.out.println("Setting " + AudioSystem.getMixer(info).getMixerInfo());
                break;
            }
        }
    }

    public ArrayList<DetectedFrequency> getDetectedFrequencies() {
        return detectedFrequencies;
    }

    private void setNewMixer(Mixer mixer) throws LineUnavailableException,
            UnsupportedAudioFileException {

        if (dispatcher != null) {
            dispatcher.stop();
        }
        currentMixer = mixer;

        float sampleRate = 44100;
        int bufferSize = 1024;
        int overlap = 0;

        System.out.println("Started listening with " + SnifferUtils.toLocalString(mixer.getMixerInfo().getName()) + "\n");

        final AudioFormat format = new AudioFormat(sampleRate, 16, 1, true,
                true);
        final DataLine.Info dataLineInfo = new DataLine.Info(
                TargetDataLine.class, format);
        TargetDataLine line;
        line = (TargetDataLine) mixer.getLine(dataLineInfo);
        final int numberOfSamples = bufferSize;
        line.open(format, numberOfSamples);
        line.start();
        final AudioInputStream stream = new AudioInputStream(line);

        JVMAudioInputStream audioStream = new JVMAudioInputStream(stream);
        dispatcher = new AudioDispatcher(audioStream, bufferSize,
                overlap);

        dispatcher.addAudioProcessor(new PitchProcessor(algo, sampleRate, bufferSize, this));

        new Thread(dispatcher, "Audio dispatching").start();
    }

    @Override
    public void handlePitch(PitchDetectionResult pitchDetectionResult, AudioEvent audioEvent) {
        if (pitchDetectionResult.getPitch() != -1) {
            double timeStamp = audioEvent.getTimeStamp();
            float pitch = pitchDetectionResult.getPitch();
            float probability = pitchDetectionResult.getProbability();
            double rms = audioEvent.getRMS() * 100;
            String message = String.format("Pitch detected at %.2fs: %.2fHz ( %.2f probability, RMS: %.5f )\n", timeStamp, pitch, probability, rms);
            System.out.println(message);
            DetectedFrequency detectedFrequency = new DetectedFrequency((float) timeStamp, pitch);
            detectedFrequencies.add(detectedFrequency);
        }
    }
}