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

public class QueuePane extends BorderPane{
    private LoginPane loginPane;
    private boolean isAi;
    private AudioClip pokemon;
    private FadeTransition ft;


    public QueuePane(Boolean aI){
        isAi = aI;
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

    private boolean weAreFirst = false;
    private CallbackWithParam<HashMap<String,String>> onMatch = message -> {
        StandardGameType gameType;

        if (message.get("OPPONENT").equals(message.get("PLAYERTOMOVE"))) {
            System.out.println("Remote begins");
            weAreFirst = false;
            gameType = (isAi) ? ONLINE_REMOTE_VS_AI : ONLINE_REMOTE_VS_HUMAN;
        } else {
            weAreFirst = true;
            gameType = (isAi) ? ONLINE_AI_VS_REMOTE : ONLINE_HUMAN_VS_REMOTE;
        }

        if (message.get("GAMETYPE").equals("Tic-tac-toe")) {
            PlayField playField = PlayField.createGameAndPlayField(Arcade.GameFactory.TicTacToe, gameType, (game) -> setPlayerNames(game, message.get("OPPONENT")));
            CompositionRoot.getInstance().lobby.setScene(playField.getScene());
        } else { // Reversi
            PlayField playField = PlayField.createGameAndPlayField(Arcade.GameFactory.Reversi, gameType, (game) -> setPlayerNames(game, message.get("OPPONENT")));
            CompositionRoot.getInstance().lobby.setScene(playField.getScene());
        }

        ft.stop();
        pokemon.stop();

        unregister();
    };

    private void setPlayerNames(GameRules game, String aiName) {
        game.getPlayer((weAreFirst)? 0 : 1).setName(LoginPane.username);
        game.getPlayer((weAreFirst)? 1 : 0).setName(aiName);
    }



    public void unregister(){
        CompositionRoot.getInstance().connection.getFromServer().onMatch.unregister(onMatch);
    }
}
