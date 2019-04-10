package GUI;

import game_util.Arcade;
import game_util.Arcade.GameFactory;
import game_util.Arcade.PlayerFactory;
import game_util.Arcade.RefereeFactory;
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
    private LoginPane loginPane;
    private boolean humanOrAi;
    private AudioClip pokemon;
    private FadeTransition ft;


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

        pokemon = new AudioClip(this.getClass().getResource("sounds/pokemon.mp3").toExternalForm());

        pokemon.play(100);

        ft = new FadeTransition(Duration.millis(6000), this);
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
    private CallbackWithParam<HashMap<String,String>> onMatch = message -> { // TODO FIX dit is het zelfde als in de lobby
        PlayField playField;
        Scene playScene;
        Arcade arcade = CompositionRoot.getInstance().arcade;
        GameRules game;
        System.out.println(humanOrAi);

        int local = 0, remote = 1;

        if(message.get("GAMETYPE").equals("Tic-tac-toe")) {
            if (humanOrAi) {
                game = arcade.createGame(GameFactory.TicTacToe, RefereeFactory.NetworkedReferee, PlayerFactory.TicTacToeAIMiniMax, PlayerFactory.RemotePlayer);
            } else {
                game = arcade.createGame(GameFactory.TicTacToe, RefereeFactory.NetworkedReferee, PlayerFactory.HumanPlayer, PlayerFactory.RemotePlayer);
            }
            playField = new PlayField(3,3, game);
            playScene = playField.getScene();

        } else {

            PlayerFactory
                    first = PlayerFactory.RemotePlayer,
                    second = humanOrAi ? PlayerFactory.ReversiAIMiniMax : PlayerFactory.HumanPlayer;

            if(message.get("OPPONENT").equals(message.get("PLAYERTOMOVE"))) {
                System.out.println("Remote begins");
                local = 1;
                remote = 0;
                second = first;
                first = PlayerFactory.RemotePlayer;
            }

            game = arcade.createGame(GameFactory.Reversi, RefereeFactory.NetworkedReferee, first, second);
            playField = new PlayField(8, 8, game);
            playScene = playField.getScene();
        }

        game.getPlayer(local).setName(loginPane.username);
        game.getPlayer(remote).setName(message.get("OPPONENT"));
        CompositionRoot.getInstance().lobby.setScene(playScene);
        game.onValidMovePlayed.register((pair0 -> {
            System.out.println(game);
            Platform.runLater(() -> playField.setPicture(game, pair0.getKey(), pair0.getValue()));
        }));

        game.onGameEnded.register(() -> {
            Platform.runLater(() -> playField.displayWinScreen(game.getGameState()));
        });

        game.start(true); // Start the game in a new thread
        System.out.println("Continue");
        ft.stop();
        pokemon.stop();

        unregister();
    };



    public void unregister(){
        CompositionRoot.getInstance().connection.getFromServer().onMatch.unregister(onMatch);
    }
}
