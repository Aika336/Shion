import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws LineUnavailableException, IOException {
        Client client = new Client("localhost", 12345);

        client.start();
    }
}