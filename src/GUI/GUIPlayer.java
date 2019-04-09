package GUI;

import game_util.GameRules;
import game_util.Move;
import game_util.Player;
import util.CompositionRoot;

public class GUIPlayer extends Player {

    Lobby gui;
    GameRules game;

    public GUIPlayer(GameRules game) {
        gui = CompositionRoot.getInstance().lobby;
        this.game = game;
    }

    //TODO get en set fixen
    @Override
    public Move getInput() {
        int paneNr = 1;//gui.getPaneNr();
        while (true) {
            if (paneNr != -1) {
                //gui.resetPaneNR();
                return game.getMove(paneNr, getNr());
            }
        }
    }
}
