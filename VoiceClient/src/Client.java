import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client extends Thread {
    private Microphone microphone;
    private Speaker speaker;
    private Socket socket;
    private OutputStream outStream;
    private InputStream inStream;
    private boolean running = true;
    private final int byteRate = 512;

    public Client(String ip, int port) throws IOException, LineUnavailableException {
        socket = new Socket(ip, port);
        microphone = new Microphone();
        speaker = new Speaker();
        outStream = socket.getOutputStream();
        inStream = socket.getInputStream();
    }

    @Override
    public void run() {
        new Thread(() -> {
            try {
                sendMicrophone();
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    stopClient();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }).start();

        new Thread(() -> {
            try {
                receiveAudio();
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    stopClient();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }).start();
    }


    private void sendMicrophone() throws LineUnavailableException, IOException {
        microphone.start();

        byte[] buffer = new byte[byteRate];
        while (running) {
            microphone.readBufferUntilFull(buffer);
            outStream.write(buffer);
        }
    }

    private void receiveAudio() throws IOException {
        speaker.start();
        byte[] buffer = new byte[byteRate];
        int bytesRead;
        while(running && (bytesRead = inStream.read(buffer)) != -1) {
            speaker.play(buffer, bytesRead);
        }
    }

    private void stopClient() throws IOException {
        running = false;
        microphone.stop();
        speaker.stop();

        outStream.close();
        inStream.close();
        socket.close();
    }
}