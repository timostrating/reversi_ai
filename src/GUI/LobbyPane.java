package GUI;

import game_util.Arcade;
import game_util.GameRules;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import network.Connection;
import util.ArrayUtils;
import util.CallbackWithParam;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import util.CompositionRoot;

import java.util.HashMap;
import java.util.Optional;

public class LobbyPane extends GridPane {
    Connection connection;
    ChoiceBox gameList;
    ListView<String> playerList;
    GridPane listGrid;
    GridPane buttonGrid;

    public LobbyPane() {

        this.setHgap(5);
        this.setVgap(5);
        this.setPadding(new Insets(15,15,15,15));

        listGrid = new GridPane();
        buttonGrid = new GridPane();

        listGrid.setVgap(10);

        buttonGrid.setVgap(10);
        buttonGrid.setHgap(5);
        buttonGrid.setPadding(new Insets(0,15,15,15));

        connection = CompositionRoot.getInstance().connection;

        new Thread(() -> {
            while(true){
                try {
                    connection.getFromServer().onPlayerList.register(onPlayerList);
                    connection.getToServer().getPlayerList();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        connection.getFromServer().onChallenge.register(onChallenge);

        //Buttons
        Label onlinePlayers = new Label("Online spelers");
        Label withAi = new Label("Met de Ai spelen");
        Label gameType = new Label("Spel selecteren");
        Button spel1 = new Button("Tic Tac Toe");
        Button spel2 = new Button("Reversi");
        Button queue = new Button("Zoeken naar spel");
        Button challenge = new Button("Speler uitdagen");
        CheckBox humanOrAi = new CheckBox();

        humanOrAi.setText("Menselijke speler");

        humanOrAi.setOnAction(event -> {
            if(humanOrAi.selectedProperty().getValue())
                humanOrAi.setText("Ai speler");
            else
                humanOrAi.setText("Menselijke speler");
        });

        listGrid.setHalignment(onlinePlayers, HPos.CENTER);

        connection.getFromServer().onGameList.register(onGameList);
        connection.getToServer().getGameList();

        listGrid.add(onlinePlayers, 0,0);

        buttonGrid.add(gameType, 0,0);
        buttonGrid.add(withAi, 0,2);
        buttonGrid.add(humanOrAi, 0,3);
        buttonGrid.add(queue, 0,4);
        buttonGrid.add(challenge, 0,5);
        buttonGrid.add(spel1, 0, 6);
        buttonGrid.add(spel2, 0,7);

        this.add(listGrid, 0, 0);
        this.add(buttonGrid, 1, 0);
//                spel2, queue, challenge, humanOrAi);

        // Games
        PlayField ticTacToe = new PlayField(3,3);
        PlayField reversi = new PlayField(8,8);

        //Scenes
        Scene ticTacToeScene = ticTacToe.getScene();
        Scene reversiScene = reversi.getScene();

        //challenge button
        challenge.setOnAction(event -> {
            connection.getToServer().setChallenge(playerList.getSelectionModel().getSelectedItem(), (String) gameList.getSelectionModel().getSelectedItem());
        });

        //queue button
        queue.setOnAction(event -> {
            connection.getToServer().subscribeGame((String) gameList.getSelectionModel().getSelectedItem());
            System.out.println(humanOrAi.selectedProperty().getValue());
            BorderPane QueuePane = new QueuePane();
            Scene scene = new Scene(QueuePane, 500, 400);
            CompositionRoot.getInstance().lobby.setScene(scene);
        });

        //Register Buttons
        spel1.setOnAction(event -> {
            CompositionRoot.getInstance().lobby.setScene(ticTacToeScene);
            Arcade arcade = CompositionRoot.getInstance().arcade;
            GameRules game = arcade.createGame(Arcade.GameFactory.TicTacToe, Arcade.RefereeFactory.DefaultReferee, Arcade.PlayerFactory.HumanPlayer, Arcade.PlayerFactory.HumanPlayer);

            game.onValidMovePlayed.register((pair0 -> {
                System.out.println(game);
                Platform.runLater(() -> ticTacToe.setPicture(game, pair0.getKey(), pair0.getValue()));
            }));

            game.onGameEnded.register(() -> {
                Platform.runLater(() -> ticTacToe.displayWinScreen(game.getGameState()));
            });


            new Thread(game).start();
        });

        spel2.setOnAction(event -> {
            CompositionRoot.getInstance().lobby.setScene(reversiScene);
            Arcade arcade = CompositionRoot.getInstance().arcade;
            GameRules game = arcade.createGame(Arcade.GameFactory.Reversi, Arcade.RefereeFactory.DefaultReferee, Arcade.PlayerFactory.HumanPlayer, Arcade.PlayerFactory.ReversiAIMiniMax);

            game.onValidMovePlayed.register(i -> {
                System.out.println(game);
                Platform.runLater(() -> ticTacToe.setPicture(game, i.getKey(), i.getValue()));
            });

            new Thread(game).start();
        });

    }

    private void gameTypesToBox(String[] message) {
        gameList = new ChoiceBox();
        for(String game : message){
            gameList.getItems().add(game);
        }
        gameList.setValue(message[0]);
        connection.getFromServer().onGameList.unregister(onGameList);
        Platform.runLater(() -> buttonGrid.add(gameList, 0,1));
    }

    private void playerListToList(String[] message){
        Platform.runLater(() -> {
            if(playerList == null) {
                playerList = new ListView<String>();
                playerList.setPrefWidth(200);
                playerList.setPrefHeight(200);
                listGrid.add(playerList, 0,1);
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

    private void getChallenge(HashMap<String, String> message){
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Inkomende uitdaging");
            alert.setHeaderText(null);
            alert.setContentText("Wil je een potje " + message.get("GAMETYPE") + " spelen met " + message.get("CHALLENGER"));

            ButtonType buttonYes = new ButtonType("Ja");
            ButtonType buttonNo = new ButtonType("Nee");

            alert.getButtonTypes().setAll(buttonYes, buttonNo);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonYes){
                connection.getToServer().setChallengeAccept(Integer.parseInt(message.get("CHALLENGENUMBER")));
                connection.getFromServer().onChallenge.unregister(onChallenge);
            } else {
                alert.close();
            }
        });
    }

    CallbackWithParam<String[]> onGameList = this::gameTypesToBox;
    CallbackWithParam<String[]> onPlayerList = this::playerListToList;
    CallbackWithParam<HashMap<String, String>> onChallenge = this::getChallenge;
}
