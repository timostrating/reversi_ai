package GUI;


import game_util.GameRules;
import game_util.Move;
import game_util.Player;
import util.CompositionRoot;

public class GUIPlayer extends Player {

    PlayField guiGame;
    GameRules game;

    public GUIPlayer(GameRules game) {
        guiGame = CompositionRoot.getInstance().lobby.playField;
        this.game = game;
    }

    //TODO get en set fixen
    @Override
    public Move getInput() {
        guiGame.currentTurnIsGuiPlayersTurn();

        while (true) {
            int paneNr = guiGame.getGuiPlayerInputAndReset();
            if (paneNr != -1) {
                return game.getMove(paneNr, getNr());
            }
        }
    }
}