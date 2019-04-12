package GUI;

import GUI.PlayField.StandardGameType;
import game_util.Arcade;
import game_util.GameRules;
import javafx.animation.FadeTransition;
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

import static GUI.PlayField.StandardGameType.*;

class QueuePane extends BorderPane{
    private boolean isAi;
    private AudioClip pokemon;
    private FadeTransition ft;


    QueuePane(Boolean aI){
        isAi = aI;

        CompositionRoot.getInstance().connection.getFromServer().onMatch.register(onMatch); // register to online events

        // creating a gridpane and set padding, h and v gap
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(270, 20, 20, 20));
        gridPane.setHgap(5);
        gridPane.setVgap(5);


        this.setStyle("-fx-background-image: url(\"GUI/pictures/kermitTea.gif\"); -fx-background-size: stretch;"); //set background image to the borderpane

        Button cancel = new Button("Stop met zoeken"); // create cancel button

        gridPane.add(cancel, 0,0, 50, 1); // add button to the gridpane

        this.getChildren().add(gridPane); // add gridpane to the borderpane

        pokemon = new AudioClip(this.getClass().getResource("sounds/pokemon.mp3").toExternalForm()); //pokemon song as audioclip
        pokemon.play(100);

        ft = new FadeTransition(Duration.millis(6000), this); // fade transitions for the background image
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();

        // action on pressing cancel button
        cancel.setOnAction(event -> {
            GridPane flowPane = new LobbyPane();
            Scene scene = new Scene(flowPane, 576, 316);
            CompositionRoot.getInstance().lobby.setScene(scene);
            ft.stop();
            pokemon.stop();
        });
    }


    private boolean weAreFirst = false;
    private CallbackWithParam<HashMap<String,String>> onMatch = message -> { // action on receiving a match
        StandardGameType gameType;

        if (message.get("OPPONENT").equals(message.get("PLAYERTOMOVE"))) { // look if the opponent has first turn
            System.out.println("Remote begins");
            weAreFirst = false;
            gameType = (isAi) ? ONLINE_REMOTE_VS_AI : ONLINE_REMOTE_VS_HUMAN;
        } else {
            weAreFirst = true;
            gameType = (isAi) ? ONLINE_AI_VS_REMOTE : ONLINE_HUMAN_VS_REMOTE;
        }

        // getting what game to play
        if (message.get("GAMETYPE").equals("Tic-tac-toe")) {
            PlayField playField = PlayField.createGameAndPlayField(Arcade.GameFactory.TicTacToe, gameType, (game) -> setPlayerNames(game, message.get("OPPONENT")));
            CompositionRoot.getInstance().lobby.setScene(playField.getScene());
        } else { // Reversi
            PlayField playField = PlayField.createGameAndPlayField(Arcade.GameFactory.Reversi, gameType, (game) -> setPlayerNames(game, message.get("OPPONENT")));
            CompositionRoot.getInstance().lobby.setScene(playField.getScene());
        }

        // stopping all events and unregister to the online events
        ft.stop();
        pokemon.stop();
        unregister();
    };

    // set player one and two
    private void setPlayerNames(GameRules game, String aiName) {
        game.getPlayer((weAreFirst)? 0 : 1).setName(LoginPane.username);
        game.getPlayer((weAreFirst)? 1 : 0).setName(aiName);
    }

    // unregister to online events
    private void unregister(){
        CompositionRoot.getInstance().connection.getFromServer().onMatch.unregister(onMatch);
    }
}
