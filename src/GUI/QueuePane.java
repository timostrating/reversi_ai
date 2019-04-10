package GUI;

import game_util.Arcade;
import game_util.GameRules;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.media.AudioClip;
import javafx.util.Duration;
import util.CallbackWithParam;
import util.CompositionRoot;


import java.util.HashMap;

public class QueuePane extends BorderPane{
    LoginPane loginPane;
    boolean humanOrAi;

    public QueuePane(Boolean aI){
        humanOrAi = aI;
        CompositionRoot.getInstance().connection.getFromServer().onMatch.register(onMatch);

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(350, 20, 20, 20));
        gridPane.setHgap(5);
        gridPane.setVgap(5);


        this.setStyle("-fx-background-image: url(\"GUI/pictures/kermitTea.gif\"); -fx-background-size: cover;");

        Button cancel = new Button("Stop met zoeken");

        gridPane.add(cancel, 0,0, 50, 1);

        this.getChildren().add(gridPane);

        String musicFile = "sounds/pokemon.mp3";

        AudioClip pokemon = new AudioClip(this.getClass().getResource("sounds/pokemon.mp3").toExternalForm());

        pokemon.play(100);

        FadeTransition ft = new FadeTransition(Duration.millis(2000), this);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();

        cancel.setOnAction(event -> {
            GridPane flowPane = new LobbyPane();
            Scene scene = new Scene(flowPane, 550, 300);
            CompositionRoot.getInstance().lobby.setScene(scene);
            ft.stop();
            pokemon.stop();
        });
    }
    private CallbackWithParam<HashMap<String,String>> onMatch = message -> {
        PlayField playField;
        Scene playScene;
        Arcade arcade = CompositionRoot.getInstance().arcade;
        GameRules game;
        System.out.println(humanOrAi);
        if(message.get("GAMETYPE").equals("Tic-tac-toe")) {
            playField = new PlayField(3,3);
            playScene = playField.getScene();
            if (humanOrAi) {
                game = arcade.createGame(Arcade.GameFactory.TicTacToe, Arcade.RefereeFactory.NetworkedReferee, Arcade.PlayerFactory.TicTacToeAIMiniMax, Arcade.PlayerFactory.RemotePlayer);
            } else {
                game = arcade.createGame(Arcade.GameFactory.TicTacToe, Arcade.RefereeFactory.NetworkedReferee, Arcade.PlayerFactory.HumanPlayer, Arcade.PlayerFactory.RemotePlayer);
            }

        } else {

            playField = new PlayField(8, 8);
            playScene = playField.getScene();
            if (humanOrAi){
                game = arcade.createGame(Arcade.GameFactory.Reversi, Arcade.RefereeFactory.NetworkedReferee, Arcade.PlayerFactory.ReversiAIMiniMax, Arcade.PlayerFactory.RemotePlayer);
            } else {
                game = arcade.createGame(Arcade.GameFactory.Reversi, Arcade.RefereeFactory.NetworkedReferee, Arcade.PlayerFactory.HumanPlayer, Arcade.PlayerFactory.RemotePlayer);
            }
        }
        game.getPlayer(0).setName(loginPane.username);
        game.getPlayer(1).setName(message.get("OPPONENT"));
        CompositionRoot.getInstance().lobby.setScene(playScene);
        game.onValidMovePlayed.register((pair0 -> {
            System.out.println(game);
            Platform.runLater(() -> playField.setPicture(game, pair0.getKey(), pair0.getValue()));
        }));

        game.onGameEnded.register(() -> {
            Platform.runLater(() -> playField.displayWinScreen(game.getGameState()));
        });

        new Thread(game).start();
        unregister();
    };



    public void unregister(){
        CompositionRoot.getInstance().connection.getFromServer().onMatch.unregister(onMatch);
    }
}
