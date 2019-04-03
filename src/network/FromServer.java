package network;

import util.CallbackWithParam;
import util.Delegate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;

public class FromServer implements Runnable {
    public final Delegate<CallbackWithParam<HashMap<String, String>>> onChallengeCanceled = new Delegate<>();
    public final Delegate<CallbackWithParam<HashMap<String, String>>> onChallenge = new Delegate<>();
    public final Delegate<CallbackWithParam<HashMap<String, String>>> onMatch = new Delegate<>();
    public final Delegate<CallbackWithParam<HashMap<String, String>>> onTurn = new Delegate<>();
    public final Delegate<CallbackWithParam<HashMap<String, String>>> onMove = new Delegate<>();
    public final Delegate<CallbackWithParam<HashMap<String, String>>> onResult = new Delegate<>();
    public final Delegate<CallbackWithParam<String[]>> onPlayerList = new Delegate<>();
    public final Delegate<CallbackWithParam<String[]>> onGameList = new Delegate<>();
    public final Delegate<CallbackWithParam<String>> onHelp = new Delegate<>();
    public final Delegate<CallbackWithParam<String>> onError = new Delegate<>();
    public final Delegate<CallbackWithParam<String>> onOk = new Delegate<>();

    BufferedReader fromServer;

    public FromServer(Socket socket) throws IOException {
        fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public HashMap toHashMap(String input){
        HashMap<String, String> map = new HashMap<>();
        if(input.startsWith("SVR GAME WIN")){
            map.put("GAME", "WIN");
        } else if(input.startsWith("SVR GAME DRAW")){
            map.put("GAME", "DRAW");
        } else if(input.startsWith("SVR GAME LOSS")){
            map.put("GAME", "LOSS");
        }
        input = input.substring(input.indexOf("{") + 1, input.indexOf("}"));
        input = input.replace("\"", "");
        String[] keyValuePairs = input.split(",");
        for(String pair: keyValuePairs){
            String[] key = pair.split(": ");
            map.put(key[0].trim(), key[1].trim());
        }
        return map;
    }

    public String[] toStringArray(String input){
        String[] stringArray;
        input = input.substring(input.indexOf("[") + 1, input.indexOf("]"));
        input = input.replace("\"", "");
        stringArray = input.split(", ");

        return stringArray;
    }

    public void challengeCanceled(String cancel){
        onChallengeCanceled.notifyObjects(o -> o.callback(toHashMap(cancel)));
    }

    public void challenge(String invite){
        onChallenge.notifyObjects(o -> o.callback(toHashMap(invite)));
    }

    public void yourTurn(String turn){
        onTurn.notifyObjects(o -> o.callback(toHashMap(turn)));
    }

    public void gameMatch(String match){
        onMatch.notifyObjects(o -> o.callback(toHashMap(match)));
    }

    public void playerList(String players){
        onPlayerList.notifyObjects(o -> o.callback(toStringArray(players)));
    }

    public void gameList(String games){
        onGameList.notifyObjects(o -> o.callback(toStringArray(games)));
    }

    public void move(String move){
        onMove.notifyObjects(o -> o.callback(toHashMap(move)));
    }

    public void result(String result){
        onResult.notifyObjects(o -> o.callback(toHashMap(result)));
    }

    @Override
    public void run() {
        while (true) {
            try {
                String input = fromServer.readLine();
                if (input.startsWith("SVR GAME CHALLENGE CANCELLED")) {
                    challengeCanceled(input);
                } else if (input.startsWith("SVR GAME CHALLENGE")) {
                    challenge(input);
                } else if (input.startsWith("SVR GAME YOURTURN")) {
                    yourTurn(input);
                } else if (input.startsWith("SVR GAME MATCH")) {
                    gameMatch(input);
                } else if (input.startsWith("SVR PLAYERLIST")){
                    playerList(input);
                } else if (input.startsWith("SVR GAME MOVE")) {
                    move(input);
                } else if (input.startsWith("SVR GAMELIST")){
                    gameList(input);
                } else if (input.startsWith("SVR GAME")) {
                    result(input);
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
