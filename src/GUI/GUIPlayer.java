package GUI;

import game_util.Player;
import util.CompositionRoot;

public class GUIPlayer extends Player {

    Lobby gui;

    public GUIPlayer() {
        gui = CompositionRoot.getInstance().lobby;
    }

    @Override
    public int getInput() {
        int paneNr = 1;//gui.getPaneNr();
        while (true) {
            if (paneNr != -1) {
                //gui.resetPaneNR();
                return paneNr;
            }
        }
    }
}
