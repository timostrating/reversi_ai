package netwerk;

import java.io.IOException;
import java.net.Socket;

public class Connection {
    final String host = "localhost";
    final int port = 7789;

    private FromServer fromServer;
    private ToServer toServer;

    public Connection() {
        connect(host, port);
    }

    public void connect(String host, int port) {
        try {
            Socket socket = new Socket(host, port);
            fromServer = new FromServer(socket);
            toServer = new ToServer(socket);

            Thread t1 = new Thread(fromServer);
            Thread t2 = new Thread(toServer);

            t1.start();
            t2.start();
        } catch (IOException e){
            e.printStackTrace();
        }

    }

    public FromServer getFromServer() {
        return fromServer;
    }

    public ToServer getToServer() {
        return toServer;
    }
}