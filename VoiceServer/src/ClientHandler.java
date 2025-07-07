import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientHandler extends Thread {
    private Server server;
    private Socket client;
    private InputStream inStream;
    public OutputStream outStream;
    private final int byteRate = 960 * 2;

    public static int count = 0;

    public ClientHandler(Socket client, Server server) {
        this.server = server;
        this.client = client;

        try {
            inStream = client.getInputStream();
            outStream = client.getOutputStream();
        } catch (IOException e) {
            close();
        }

        count++;
        System.out.println("Client #" + count + " connected");
    }

    @Override
    public void run() {
        byte[] buffer = new byte[byteRate];
        int bytesRead;

        try {
            while ((bytesRead = inStream.read(buffer)) != -1) {
                server.sendData(buffer, bytesRead, this);
            }
        } catch (IOException e) {
            close();
        }
    }

    public void close() {
        try {
            inStream.close();
            outStream.close();
            server.clients.remove(this);
            client.close();
            count--;
            System.out.println("Client disconnected");
        } catch (IOException e) {
        }
    }

}
