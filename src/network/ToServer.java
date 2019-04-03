package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ToServer implements Runnable{
    PrintWriter toServer;
    BufferedReader userInput;

    public ToServer(Socket socket) throws IOException {
        toServer = new PrintWriter(socket.getOutputStream(), true);
        userInput = new BufferedReader(new InputStreamReader(System.in));
    }

//    public String[] getGameList() {
//        String[] gameList;
//        String gameString;
//
//        toServer.println("get gamelist");
//        try {
//            fromServer.readLine();
//            gameString = fromServer.readLine();
//            gameString = gameString.substring(gameString.indexOf("[") + 1, gameString.indexOf("]") - 1);
//            gameString = gameString.replace("\"", "");
//            gameList = gameString.split(", ");
//            return gameList;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return new String[] {};
//    }
//
//    public String[] getPlayerList(){
//        String[] playerList;
//        String playerString;
//
//        toServer.println("get playerlist");
//        try {
//            fromServer.readLine();
//            playerString = fromServer.readLine();
//            playerString = playerString.substring(playerString.indexOf("["), playerString.indexOf("]"));
//            playerString = playerString.replace("[", "");
//            playerString = playerString.replace("\"", "");
//            playerList = playerString.split(", ");
//            return playerList;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return new String[] {};
//    }
//
//    public void setForfeit(){
//        toServer.println("forfeit");
//        try {
//            fromServer.readLine();
//            System.out.println(fromServer.readLine());
//        } catch (IOException e){
//            e.printStackTrace();
//        }
//    }
//
//
//    public void setMove(int move) {
//        String myMove = "move " + move;
//        toServer.println(myMove);
//        System.out.println("Mijn zet: " + move);
//        try {
//            System.out.println(fromServer.readLine());
//        } catch (IOException e){
//            e.printStackTrace();
//        }
//    }
//
//    public void gameFound(String input, String output){
//        if(output.contains("GAME MATCH")){
//            System.out.println("Match found");
//            while (true) {
//                if (output.contains("GAME YOURTURN")) {
//                    try {
//                        String move = userInput.readLine();
//                        int y = Integer.parseInt(move);
//                        setMove(y);
//                        System.out.println("mijn zet" + y);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//    }

    @Override
    public void run() {
        while(true) {
            try {
                toServer.println(userInput.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
