import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class ConnectionTest {
    final String host = "localhost";
    final int port = 7789;
    BufferedReader fromServer;

    @Test
    void connect() {
        try {
            Socket socket = new Socket(host, port);
            fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(true){
            try {
            System.out.println(fromServer.readLine());
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}