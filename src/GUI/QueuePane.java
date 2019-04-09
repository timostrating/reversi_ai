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
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.util.Duration;
import util.CallbackWithParam;
import util.CompositionRoot;

import java.util.HashMap;

public class QueuePane extends BorderPane{

    public QueuePane(){

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

        // Games
        PlayField ticTacToe = new PlayField(3,3);
        PlayField reversi = new PlayField(8,8);

        //Panes
        Pane[] ticTacToePane = ticTacToe.getPane();
        Pane[] reversiPane = reversi.getPane();

        //Scenes
        Scene ticTacToeScene = ticTacToe.getScene();
        Scene reversiScene = reversi.getScene();

        if(message.get("GAMETYPE").equals("Tic-tac-toe")) {
            CompositionRoot.getInstance().lobby.setScene(ticTacToeScene);
            Arcade arcade = CompositionRoot.getInstance().arcade;
            GameRules game = arcade.createGame(Arcade.GameFactory.Reversi, Arcade.RefereeFactory.DefaultReferee, Arcade.PlayerFactory.HumanPlayer, Arcade.PlayerFactory.ReversiAIMiniMax);

            game.onValidMovePlayed.register(i -> {
                System.out.println(game);
                Platform.runLater(() -> {
                    ticTacToe.setPicture(game, i.getKey(), i.getValue());
                });
            });
            new Thread(game).start();
        } else if(message.get("GAMETYPE").equals("Reversi")){
            CompositionRoot.getInstance().lobby.setScene(reversiScene);
            Arcade arcade = CompositionRoot.getInstance().arcade;
            GameRules game = arcade.createGame(Arcade.GameFactory.Reversi, Arcade.RefereeFactory.DefaultReferee, Arcade.PlayerFactory.HumanPlayer, Arcade.PlayerFactory.ReversiAIMiniMax);

            game.onValidMovePlayed.register(i -> {
                System.out.println(game);
                Platform.runLater(() -> {
                    ticTacToe.setPicture(game, i.getKey(), i.getValue());
                });
            });
            new Thread(game).start();
        }

    };

}
