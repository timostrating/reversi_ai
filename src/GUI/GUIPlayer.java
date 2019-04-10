package GUI;


import game_util.GameRules;
import game_util.Move;
import game_util.Player;
import util.CompositionRoot;

public class GUIPlayer extends Player {

    Lobby guiGame;
    GameRules game;

    public GUIPlayer(GameRules game) {
        guiGame = CompositionRoot.getInstance().lobby;
        this.game = game;
    }

    @Override
    public Move getInput() {
        guiGame.playField.currentTurnIsGuiPlayersTurn();

        while (true) {
            int guiInput = guiGame.playField.guiPlayerInput;
            if (guiInput != -1) {
                guiGame.playField.resetGuiPlayerInput();
                return game.getMove(guiInput, getNr());
            }
        }
    }
}