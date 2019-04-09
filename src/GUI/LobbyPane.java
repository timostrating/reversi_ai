package GUI;

import game_util.Arcade;
import game_util.GameRules;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import network.Connection;
import util.ArrayUtils;
import util.CallbackWithParam;
import util.CompositionRoot;

public class LobbyPane extends FlowPane {
    Connection connection;
    ChoiceBox gameList;
    ListView<String> playerList;

    public LobbyPane() {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        connection.getFromServer().onPlayerList.register(onPlayerList);
                        connection.getToServer().getPlayerList();
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();

        connection = CompositionRoot.getInstance().connection;

        //Buttons
        Button spel1 = new Button("Tic Tac Toe");
        Button spel2 = new Button("Reversi");
        Button queue = new Button("Queue");
        Button challenge = new Button("Challenge player");
        CheckBox humanOrAi = new CheckBox();

        connection.getFromServer().onGameList.register(onGameList);
        connection.getToServer().getGameList();


        this.getChildren().addAll(spel1, spel2, queue, challenge, humanOrAi);

        // Games
        PlayField ticTacToe = new PlayField(3,3);
        PlayField reversi = new PlayField(8,8);

        //Panes
        Pane[] ticTacToePane = ticTacToe.getPane();
        Pane[] reversiPane = reversi.getPane();

        //Scenes
        Scene ticTacToeScene = ticTacToe.getScene();
        Scene reversiScene = reversi.getScene();

        //Register Buttons
        spel1.setOnAction(event -> {
            CompositionRoot.getInstance().lobby.setScene(ticTacToeScene);
            Arcade arcade = CompositionRoot.getInstance().arcade;
            GameRules game = arcade.createGame(Arcade.GameFactory.TicTacToe, Arcade.RefereeFactory.DefaultReferee, Arcade.PlayerFactory.HumanPlayer, Arcade.PlayerFactory.TicTacToeAIMiniMax);

            game.onValidMovePlayed.register(i -> {
                System.out.println(game);
                Platform.runLater(() -> {
                    ticTacToe.setPicture(game, i.getKey(), i.getValue());
                });
            });


            // TODO: Kan dit weg?
//            game.onValidMovePlayed.register((i)-> {
//
//
//                Platform.runLater(() -> {
//                    ticTacToePane[i % 3][i / 3].getChildren().add(PlayField.Anims.getPicture("x"));
//                });
//
//            }); // TODO: hardcoded size  and nr 1 ?
            new Thread(game).start();
        });

        spel2.setOnAction(event -> {
            CompositionRoot.getInstance().lobby.setScene(reversiScene);
            Arcade arcade = CompositionRoot.getInstance().arcade;
            GameRules game = arcade.createGame(Arcade.GameFactory.Reversi, Arcade.RefereeFactory.DefaultReferee, Arcade.PlayerFactory.HumanPlayer, Arcade.PlayerFactory.ReversiAIMiniMax);

            game.onValidMovePlayed.register(i -> {
                System.out.println(game);
                Platform.runLater(() -> {
                    ticTacToe.setPicture(game, i.getKey(), i.getValue());
                });
            });


            // TODO: Kan dit weg?
//            game.onValidMovePlayed.register((i)-> {
//
//
//                Platform.runLater(() -> {
//                    reversiPane[i % 3][i / 3].getChildren().add(PlayField.Anims.getPicture("black"));
//                });
//
//            }); // TODO: hardcoded size  and nr 1 ?
            new Thread(game).start();
        });


        queue.setOnAction(event -> {
            connection.getToServer().subscribeGame((String) gameList.getSelectionModel().getSelectedItem());
        });
    }

    private void gameTypesToBox(String[] message) {
        gameList = new ChoiceBox();
        for(String game : message){
            gameList.getItems().add(game);
        }
        gameList.setValue(message[0]);
        connection.getFromServer().onGameList.unregister(onGameList);
        Platform.runLater(() -> this.getChildren().add(gameList));
    }

    private void playerListToList(String[] message){
        Platform.runLater(() -> {
            if(playerList == null) {
                playerList = new ListView<String>();
                this.getChildren().add(playerList);
            }

            for(String name : message) {
                if (!playerList.getItems().contains(name)) {
                    playerList.getItems().add(name);
                }
            }
            ObservableList<String> items = playerList.getItems();
            for (int i = items.size() - 1; i >= 0; i--) {
                String name = items.get(i);
                if (!ArrayUtils.contains(message, name)) {
                    playerList.getItems().remove(name);
                }
            }
            connection.getFromServer().onGameList.unregister(onPlayerList);
        });
    }

    CallbackWithParam<String[]> onGameList = this::gameTypesToBox;
    CallbackWithParam<String[]> onPlayerList = this::playerListToList;
}
