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
import util.StringUtils;

import java.util.HashMap;
import java.util.Optional;

import static GUI.PlayField.StandardGameType.OFFLINE_AI_VS_PLAYER;

class LobbyPane extends GridPane {
    private Connection connection;
    private ComboBox<String> gameList;
    private ListView<String> playerList;
    private GridPane listGrid;
    private GridPane buttonGrid;

    LobbyPane() {

        this.getStylesheets().add("/GUI/lobbyPaneStyle.css"); //get stylesheet for the gridpane

        // setting horizontal and vertical gap for the primary gridpane
        this.setHgap(5);
        this.setVgap(5);

        this.setPadding(new Insets(15,15,15,15)); // set the padding for the primary gridpane

        // make a list and button gridpane
        listGrid = new GridPane();
        buttonGrid = new GridPane();

        listGrid.setVgap(10); // set the vertical gap for the list gridpane

        // set the horizontal and vertical gap for the button gridpane
        buttonGrid.setVgap(10);
        buttonGrid.setHgap(5);

        buttonGrid.setPadding(new Insets(0,15,15,15)); // set padding for button gridpane

        connection = CompositionRoot.getInstance().connection; // getting connection from the compositionroot

        updateOnlinePlayers(); // update online player list

        registerOnlineEvents(); // register for online events

        connection.getToServer().getGameList(); // get the gamelist from the server

        // create labels for list gridpane
        Label onlinePlayersLabel = new Label("Online spelers");
        Label usernameLabel = new Label("Ingelogd als: " + StringUtils.capitalize(LoginPane.username));

        // create labels for button gridpane
        Label gameTypeLabel = new Label("Spel selecteren");
        Label onlineLabel = new Label("                     Online spelen                     ");
        Label offlineLabel = new Label("                     Offline spelen                     ");
        Label isAiLabel = new Label("Met Ai spelen");

        // create buttons for button gridpane
        Button ticTacToeButton = new Button("Tic Tac Toe");
        Button reversiButton = new Button("Reversi");
        Button queueButton = new Button("Zoeken naar spel");
        Button challengeButton = new Button("Speler uitdagen");
        Button logoutButton = new Button("Uitloggen");

        CheckBox isAiCheckBox = new CheckBox(); // create checkbox for button gridpane

        // Set the position for labels and buttons to the center
        GridPane.setHalignment(onlinePlayersLabel, HPos.CENTER);
        GridPane.setHalignment(offlineLabel, HPos.CENTER);
        GridPane.setHalignment(reversiButton, HPos.CENTER);
        GridPane.setHalignment(ticTacToeButton, HPos.CENTER);
        GridPane.setHalignment(onlineLabel, HPos.CENTER);
        GridPane.setHalignment(usernameLabel, HPos.CENTER);
        GridPane.setHalignment(gameTypeLabel, HPos.CENTER);
        GridPane.setHalignment(isAiLabel, HPos.CENTER);
        GridPane.setHalignment(isAiCheckBox, HPos.CENTER);

        onlineLabel.setPadding(new Insets(5, 0,0,0));
        onlinePlayersLabel.setPadding(new Insets(5, 0,0,0));

        // set style for labels and primary pane
        this.setStyle("-fx-background-image: url(\"GUI/pictures/kermitBack.jpg\");  \n" +
                "-fx-background-repeat: stretch;   \n" +
                "-fx-background-size: 576 316;\n" +
                "-fx-background-position: center center;\n");

        listGrid.setStyle("-fx-background-color: rgb(255,255,255,0.9); -fx-background-radius: 5;");
        buttonGrid.setStyle("-fx-background-color: rgb(255,255,255,0.9); -fx-background-radius: 5;");

        // add labels to the list gridpane
        listGrid.add(onlinePlayersLabel, 0,0);
        listGrid.add(usernameLabel, 0, 2);

        // add labels to the button gridpane
        buttonGrid.add(isAiLabel, 1,1);
        buttonGrid.add(onlineLabel, 0, 0, 2,1);
        buttonGrid.add(gameTypeLabel, 0,1);
        buttonGrid.add(offlineLabel, 0, 4, 2,1);

        // add buttons to the button gridpane
        buttonGrid.add(queueButton, 0,3);
        buttonGrid.add(challengeButton, 1,3);
        buttonGrid.add(ticTacToeButton, 0, 5);
        buttonGrid.add(reversiButton, 1,5);
        buttonGrid.add(logoutButton, 1, 6);

        buttonGrid.add(isAiCheckBox, 1,2); // add checkbox to button gridpane

        // add list gridpane and button gridpane to the primary gridpane
        this.add(listGrid, 0, 0);
        this.add(buttonGrid, 1, 0);

        // action on pressing the challenge button
        challengeButton.setOnAction(event -> {
            connection.getToServer().setChallenge(playerList.getSelectionModel().getSelectedItem(), gameList.getSelectionModel().getSelectedItem());
            BorderPane QueuePane = new QueuePane(isAiCheckBox.selectedProperty().getValue());
            Scene scene = new Scene(QueuePane, 576, 316);
            CompositionRoot.getInstance().lobby.setScene(scene);
        });

        // action on pressing the queue button
        queueButton.setOnAction(event -> {
            connection.getToServer().subscribeGame(gameList.getSelectionModel().getSelectedItem());
            BorderPane QueuePane = new QueuePane(isAiCheckBox.selectedProperty().getValue());
            Scene scene = new Scene(QueuePane, 576, 316);
            CompositionRoot.getInstance().lobby.setScene(scene);
        });

        // action on pressing game Button
        ticTacToeButton.setOnAction(event -> {
            PlayField playField = PlayField.createGameAndPlayField(Arcade.GameFactory.TicTacToe, OFFLINE_AI_VS_PLAYER);
            CompositionRoot.getInstance().lobby.setScene(playField.getScene());
        });
        reversiButton.setOnAction(event -> {
            PlayField playField = PlayField.createGameAndPlayField(Arcade.GameFactory.Reversi, OFFLINE_AI_VS_PLAYER);
            CompositionRoot.getInstance().lobby.setScene(playField.getScene());
        });

        // action on pressing the logout Button
        logoutButton.setOnAction(e -> {
            connection.closeConnection();
            connection.getToServer().setLogout();
            BorderPane loginPane = new LoginPane();
            Scene scene = new Scene(loginPane, 576, 316);
            CompositionRoot.getInstance().lobby.setScene(scene);
        });
    }

    // update the onlineplayer list
    private void updateOnlinePlayers(){
        new Thread(() -> {
            boolean started = false, currScene = false;
            while(currScene || !started){
                currScene = CompositionRoot.getInstance().lobby.getPrimaryStage().getScene() == getScene();
                started |= currScene;
                if (started) try {
                    connection.getFromServer().onPlayerList.register(onPlayerList);
                    connection.getToServer().getPlayerList();
                    Thread.sleep(3000);
                } catch (InterruptedException ignored) { }
            }
        }).start();
    }

    // register to all the online events
    private void registerOnlineEvents(){
        connection.getFromServer().onChallenge.register(onChallenge);
        connection.getFromServer().onGameList.register(onGameList);
    }

    // getting games from the server and putting them in a combobox
    private void gameTypesToBox(String[] message) {
        gameList = new ComboBox<>();
        for(String game : message){
            gameList.getItems().add(game);
        }
        gameList.setValue(message[0]);
        connection.getFromServer().onGameList.unregister(onGameList);
        Platform.runLater(() -> buttonGrid.add(gameList, 0,2));
    }

    // getting the online players and putting them in a listview
    private void playerListToList(String[] message){
        Platform.runLater(() -> {
            if(playerList == null) {
                playerList = new ListView<>();
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

    // getting an alert of confirmation on a challenge receive
    private void getChallenge(HashMap<String, String> message){
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Inkomende uitdaging");
            alert.setHeaderText("Wil je een potje " + message.get("GAMETYPE").toLowerCase() + " spelen met " + message.get("CHALLENGER"));

            ButtonType buttonYes = new ButtonType("Ja");
            ButtonType buttonNo = new ButtonType("Nee");
            CheckBox ai = new CheckBox("laat de Ai spelen");

            alert.getButtonTypes().setAll(buttonYes, buttonNo);
            alert.getDialogPane().setContent(ai);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonYes){
                connection.getToServer().setChallengeAccept(Integer.parseInt(message.get("CHALLENGENUMBER")));
                connection.getFromServer().onChallenge.unregister(onChallenge);
                BorderPane QueuePane = new QueuePane(ai.selectedProperty().getValue());
                Scene scene = new Scene(QueuePane, 576, 316);
                CompositionRoot.getInstance().lobby.setScene(scene);
            } else if(result.get() == buttonNo){
                alert.close();
            }
        });
    }

    // actions on all the callbacks received
    private CallbackWithParam<String[]> onGameList = this::gameTypesToBox;
    private CallbackWithParam<String[]> onPlayerList = this::playerListToList;
    private CallbackWithParam<HashMap<String, String>> onChallenge = this::getChallenge;
}
