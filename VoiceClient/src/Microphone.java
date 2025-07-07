import javax.sound.sampled.*;

public class Microphone {
    private AudioFormat format;
    private DataLine.Info info;
    private TargetDataLine microphone;

    Microphone() throws LineUnavailableException {
        this.format = new AudioFormat(44100.0f, 16, 1, true, true);
        info = new DataLine.Info(TargetDataLine.class, format);
        microphone = (TargetDataLine) AudioSystem.getLine(info);
        microphone.open(format);
    }

    public void start() throws LineUnavailableException {
        microphone.start();
        System.out.println("Microphone is enable.");
    }

    public void stop() {
        microphone.stop();
        System.out.println("Microphone is disable.");
    }

    public int read(byte[] bytes) {
        return microphone.read(bytes,  0, bytes.length);
    }

    public int readBufferUntilFull(byte[] buffer) {
        int bytesRead = 0;
        while (bytesRead < buffer.length) {
            int res = read(buffer);
            bytesRead = res > 0 ? res + bytesRead : bytesRead;
        }
        return bytesRead;
    }
}
