package netwerk;

import java.io.IOException;
import java.net.Socket;

public class Connection{
    final String host = "localhost";
    final int port = 7789;

    public void connection() {
        try {
            Socket socket = new Socket(host, port);
            FromServer fromServer = new FromServer(socket);
            ToServer toServer = new ToServer(socket);

            Thread t1 = new Thread(fromServer);
            Thread t2 = new Thread(toServer);

            t1.start();
            t2.start();
        } catch (IOException e){
            e.printStackTrace();
        }

    }
}