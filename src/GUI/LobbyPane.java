package GUI;

import game_util.Arcade;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import network.Connection;
import util.ArrayUtils;
import util.CallbackWithParam;
import util.CompositionRoot;

import java.util.HashMap;
import java.util.Optional;

import static GUI.PlayField.StandardGameType.OFFLINE_AI_VS_PLAYER;

public class LobbyPane extends GridPane {
    Connection connection;
    ChoiceBox gameList;
    ListView<String> playerList;
    GridPane listGrid;
    GridPane buttonGrid;
    LoginPane loginPane;

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
            boolean started = false, currScene = false;
            while(currScene || !started){
                currScene = CompositionRoot.getInstance().lobby.getPrimaryStage().getScene() == getScene();
                started |= currScene;
                if (started) try {
                    connection.getFromServer().onPlayerList.register(onPlayerList);
                    connection.getToServer().getPlayerList();
                    Thread.sleep(3000);
                } catch (InterruptedException e) { }
            }
        }).start();

        connection.getFromServer().onChallenge.register(onChallenge);

        //Buttons
        Label onlinePlayers = new Label("Online spelers");
        Label withAi = new Label("");
        Label gameType = new Label("Spel selecteren");
        Label username = new Label("Ingelogd als: " + loginPane.username);
        Button ticTacToeButton = new Button("Tic Tac Toe");
        Button reversiButton = new Button("Reversi");
        Button queue = new Button("Zoeken naar spel");
        Button challenge = new Button("Speler uitdagen");
        Button logout = new Button("Uitloggen");
        CheckBox isAiCheckBox = new CheckBox();

        isAiCheckBox.setText("De Ai laten spelen");

        GridPane.setHalignment(onlinePlayers, HPos.CENTER);

        connection.getFromServer().onGameList.register(onGameList);
        connection.getToServer().getGameList();

        listGrid.add(onlinePlayers, 0,0);
        listGrid.add(username, 0, 2);

        buttonGrid.add(gameType, 0,0);
        buttonGrid.add(withAi, 1,0);
        buttonGrid.add(isAiCheckBox, 1,1);
        buttonGrid.add(queue, 0,2);
        buttonGrid.add(challenge, 1,2);
        buttonGrid.add(ticTacToeButton, 0, 6);
        buttonGrid.add(reversiButton, 0,7);
        buttonGrid.add(logout, 1, 8);

        this.add(listGrid, 0, 0);
        this.add(buttonGrid, 1, 0);


        //challenge button
        challenge.setOnAction(event -> {
            connection.getToServer().setChallenge(playerList.getSelectionModel().getSelectedItem(), (String) gameList.getSelectionModel().getSelectedItem());
            queue.fire();
        });

        //queue button
        queue.setOnAction(event -> {
            connection.getToServer().subscribeGame((String) gameList.getSelectionModel().getSelectedItem());
            BorderPane QueuePane = new QueuePane(isAiCheckBox.selectedProperty().getValue());
            Scene scene = new Scene(QueuePane, 500, 400);
            CompositionRoot.getInstance().lobby.setScene(scene);
        });

        // game Button
        ticTacToeButton.setOnAction(event -> {
            PlayField playField = PlayField.createGameAndPlayField(Arcade.GameFactory.TicTacToe, OFFLINE_AI_VS_PLAYER);
            CompositionRoot.getInstance().lobby.setScene(playField.getScene());
        });
        reversiButton.setOnAction(event -> {
            PlayField playField = PlayField.createGameAndPlayField(Arcade.GameFactory.Reversi, OFFLINE_AI_VS_PLAYER);
            CompositionRoot.getInstance().lobby.setScene(playField.getScene());
        });

        //logout Button
        logout.setOnAction(e -> {
            connection.closeConnection();
            connection.getToServer().setLogout();
            BorderPane loginPane = new LoginPane();
            Scene scene = new Scene(loginPane, 576, 316);
            CompositionRoot.getInstance().lobby.setScene(scene);
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
                    if(!name.equals(LoginPane.username)) {
                        playerList.getItems().add(name);
                    }
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
            alert.setContentText("Wil je een potje " + message.get("GAMETYPE").toLowerCase() + " spelen met " + message.get("CHALLENGER"));

            ButtonType buttonYes = new ButtonType("Ja");
            ButtonType buttonNo = new ButtonType("Nee");
            CheckBox ai = new CheckBox("laat de Ai spelen");

            alert.getButtonTypes().setAll(buttonYes, buttonNo);
            alert.getDialogPane().setContent(ai);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonYes){
                connection.getToServer().setChallengeAccept(Integer.parseInt(message.get("CHALLENGENUMBER")));
                connection.getFromServer().onChallenge.unregister(onChallenge);
                connection.getToServer().subscribeGame((String) gameList.getSelectionModel().getSelectedItem());
                BorderPane QueuePane = new QueuePane(ai.selectedProperty().getValue());
                Scene scene = new Scene(QueuePane, 500, 400);
                CompositionRoot.getInstance().lobby.setScene(scene);
            } else {
                alert.close();
            }
        });
    }

    CallbackWithParam<String[]> onGameList = this::gameTypesToBox;
    CallbackWithParam<String[]> onPlayerList = this::playerListToList;
    CallbackWithParam<HashMap<String, String>> onChallenge = this::getChallenge;
}
