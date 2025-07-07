import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server extends Thread {
    public ArrayList<ClientHandler> clients = new ArrayList<>();
    private ServerSocket serverSocket;
    public int port;

    public Server(int port) throws IOException {
        this.port = port;
        serverSocket = new ServerSocket(port, 5);
    }

    @Override
    public void run() {
        System.out.println("Port " + port);
        while (true) {
            try {
                Socket sock = serverSocket.accept();
                ClientHandler client = new ClientHandler(sock, this);
                clients.add(client);
                client.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public synchronized void sendData(byte[] bytes, int bufSize, ClientHandler clin) {
        ArrayList<ClientHandler> toRemove = new ArrayList<>();
        for (var client : clients) {
            if(client == clin) continue;
            try {
                client.outStream.write(bytes, 0, bufSize);
            } catch (IOException e) {
                toRemove.add(client);
            }
        }
        clients.removeAll(toRemove);
    }
}
