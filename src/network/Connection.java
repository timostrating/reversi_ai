package network;

import util.CallbackWithParam;
import util.Delegate;

import java.io.IOException;
import java.net.Socket;

public class Connection {
    final String host = "localhost";
    final int port = 7789;

    private FromServer fromServer;
    private ToServer toServer;

    public final Delegate<CallbackWithParam<Boolean>> onConnection = new Delegate<>();

    public Connection() {
        connect(host, port);
    }

    public void connect(String host, int port) {
        try (Socket testConnection = new Socket(host, port)){
            Socket socket = new Socket(host, port);
            fromServer = new FromServer(socket);
            toServer = new ToServer(socket);

            Thread t1 = new Thread(fromServer);
            Thread t2 = new Thread(toServer);

            t1.start();
            t2.start();

            onConnection.notifyObjects(o -> o.callback(true));
        } catch (IOException e) {
            onConnection.notifyObjects(o -> o.callback(false));
        }
    }

    public FromServer getFromServer() {
        return fromServer;
    }

    public ToServer getToServer() {
        return toServer;
    }
}