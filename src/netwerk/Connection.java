package netwerk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Connection {
    final String host = "localhost";
    final int port = 7789;
    BufferedReader fromServer;
    PrintWriter toServer;
    BufferedReader userInput;

    public void connection() throws IOException {

        Socket socket = new Socket(host, port);

        fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        toServer = new PrintWriter(socket.getOutputStream(), true);
        userInput = new BufferedReader(new InputStreamReader(System.in));

        System.out.println(fromServer.readLine());
        System.out.println(fromServer.readLine());



        for(String game : getGameList()){
            System.out.println(game);
        }

        for(String player : getPlayerList()){
            System.out.println(player);
        }


        while (true) {
            String input = userInput.readLine();
            System.out.println(input);
            ready(input);
            fromServer.readLine();
        }
    }

    public String[] getGameList() throws IOException{
        String[] gameList;
        String gameString;

        toServer.println("get gamelist");
        fromServer.readLine();

        gameString = fromServer.readLine();
        gameString = gameString.substring(gameString.indexOf("[") + 1, gameString.indexOf("]") - 1);
        gameString = gameString.replace("\"", "");
        gameList = gameString.split(", ");

        return gameList;
    }

    public String[] getPlayerList() throws IOException{
        String[] playerList;
        String playerString;

        toServer.println("get playerlist");
        fromServer.readLine();

        playerString = fromServer.readLine();
        playerString = playerString.substring(playerString.indexOf("["), playerString.indexOf("]"));
        playerString = playerString.replace("[", "");
        playerString = playerString.replace("\"", "");
        playerList = playerString.split(", ");

        return playerList;
    }

    public void setForfeit(){

    }

    public void getTurn() throws IOException{
        System.out.println(fromServer.readLine());
    }

    public void setMove(String move) throws IOException{
        toServer.println(move);
        fromServer.readLine();
        getTurn();
    }

    public void getMove(){
        // stuur move
    }

    public void ready(String x) throws IOException {
        if (x.contains("SVR GAME MATCH")) {
            setMove(x);
        } else {
            toServer.println(x);
        }
    }

}