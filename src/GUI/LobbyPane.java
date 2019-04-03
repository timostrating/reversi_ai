package GUI;

import Util.Arcade;
import Util.CompositionRoot;
import Util.GameRules;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;

public class LobbyPane extends FlowPane {


    public LobbyPane() {

        //Buttons
        Button spel1 = new Button("Tic Tac Toe");
        Button spel2 = new Button("Reversi");

        this.getChildren().addAll(spel1, spel2);

        // Games
        PlayField ticTacToe = new PlayField(3,3);
        PlayField reversi = new PlayField(8,8);

        //Panes
        Pane[][] ticTacToePane = ticTacToe.getPane();
        Pane[][] reversiPane = reversi.getPane();

        //Scenes
        Scene ticTacToeScene = ticTacToe.getScene();
        Scene reversiScene = reversi.getScene();

        //Register Buttons
        spel1.setOnAction(event -> {
            CompositionRoot.getInstance().lobby.setScene(ticTacToeScene);
            Arcade arcade = CompositionRoot.getInstance().arcade;
            GameRules game = arcade.createGame(Arcade.GameFactory.TicTacToe, Arcade.RefereeFactory.DefaultReferee, Arcade.PlayerFactory.HumanPlayer, Arcade.PlayerFactory.TicTacToeAIMiniMax);

            game.onValidMovePlayed.register((i)-> {

                Platform.runLater(() -> {
                    ticTacToePane[i % 3][i / 3].getChildren().add(PlayField.Anims.getAtoms(1));
                });

            }); // TODO: hardcoded size  and nr 1 ?
            new Thread(game).start();
        });

        spel2.setOnAction(event -> {
            CompositionRoot.getInstance().lobby.setScene(reversiScene);
            Arcade arcade = CompositionRoot.getInstance().arcade;
            GameRules game = arcade.createGame(Arcade.GameFactory.TicTacToe, Arcade.RefereeFactory.DefaultReferee, Arcade.PlayerFactory.HumanPlayer, Arcade.PlayerFactory.TicTacToeAIMiniMax);

            game.onValidMovePlayed.register((i)-> {

                Platform.runLater(() -> {
                    reversiPane[i % 3][i / 3].getChildren().add(PlayField.Anims.getAtoms(1));
                });

            }); // TODO: hardcoded size  and nr 1 ?
            new Thread(game).start();
        });
    }
}
