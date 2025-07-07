import javax.sound.sampled.*;

public class Speaker {
    private SourceDataLine speakers;
    private AudioFormat format;

    public Speaker() throws LineUnavailableException {
        format = new AudioFormat(44100.0f, 16, 1, true, true);
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        speakers = (SourceDataLine) AudioSystem.getLine(info);
        speakers.open(format);
    }

    public void start() {
        speakers.start();
    }

    public void play(byte[] data, int length) {
        speakers.write(data, 0, length);
    }

    public void stop() {
        speakers.stop();
        speakers.close();
    }
}