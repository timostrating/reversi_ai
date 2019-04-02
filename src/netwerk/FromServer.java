package netwerk;

import Util.CallbackWithParam;
import Util.Delegate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;

public class FromServer implements Runnable {
    private Delegate<CallbackWithParam<String>> onChallengeCanceled = new Delegate<>();
    private Delegate<CallbackWithParam<String>> onChallenge = new Delegate<>();
    private Delegate<CallbackWithParam<String>> onMatch = new Delegate<>();
    private Delegate<CallbackWithParam<String>> onTurn = new Delegate<>();
    private Delegate<CallbackWithParam<String>> onMove = new Delegate<>();
    private Delegate<CallbackWithParam<String>> onResult = new Delegate<>();
    private Delegate<CallbackWithParam<String>> onHelp = new Delegate<>();
    private Delegate<CallbackWithParam<String>> onError = new Delegate<>();
    private Delegate<CallbackWithParam<String>> onOk = new Delegate<>();

    BufferedReader fromServer;

    public FromServer(Socket socket) throws IOException {
        fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void gameMatch(String match){
        HashMap<String, String> matchDetails = new HashMap<>();
        match = match.substring(match.indexOf("{" + 1), match.indexOf("}"));
        String[] listMatch = match.split(": ");
        for(String x : listMatch){
            System.out.println(x);
        }
        onMatch.notifyObjects(o -> o.callback(""));
    }

    @Override
    public void run() {
        while (true) {
            try {
                String input = fromServer.readLine();
                if (input.startsWith("SVR GAME CHALLENGE CANCELLED")) {
                    onChallengeCanceled.notifyObjects(o -> o.callback(input));
                } else if (input.startsWith("SVR GAME CHALLENGE")) {
                    onChallenge.notifyObjects(o -> o.callback(input));
                } else if (input.startsWith("SVR GAME YOURTURN")) {
                    onTurn.notifyObjects(o -> o.callback(input));
                } else if (input.startsWith("SVR GAME MATCH")) {
                    gameMatch(input);
                } else if (input.startsWith("SVR GAME MOVE")) {
                    onMove.notifyObjects(o -> o.callback(input));
                } else if (input.startsWith("SVR GAME")) {
                    onResult.notifyObjects(o -> o.callback(input));
                } else if (input.startsWith("SVR HELP")) {
                    onHelp.notifyObjects(o -> o.callback(input));
                } else if (input.startsWith("ERR")) {
                    onError.notifyObjects(o -> o.callback(input));
                } else if (input.startsWith("OK")) {
                    onOk.notifyObjects(o -> o.callback(input));
                } else {
                    System.out.println(input);
                }
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
