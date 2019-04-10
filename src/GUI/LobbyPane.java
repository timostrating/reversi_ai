package GUI;

import game_util.Arcade;
import game_util.GameRules;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import util.CompositionRoot;

public class LobbyPane extends FlowPane {


    public LobbyPane() {

        //Buttons
        Button spel1 = new Button("Tic Tac Toe");
        Button spel2 = new Button("Reversi");

        this.getChildren().addAll(spel1, spel2);

        // Games
        PlayField ticTacToe = new PlayField(3,3);
        PlayField reversi = new PlayField(8,8);

        //Scenes
        Scene ticTacToeScene = ticTacToe.getScene();
        Scene reversiScene = reversi.getScene();

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
}
