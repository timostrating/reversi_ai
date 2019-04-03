package GUI;

import util.CompositionRoot;
import game_util.Player;

public class GUIPlayer extends Player {

    Lobby gui;

    public GUIPlayer() {
        gui = CompositionRoot.getInstance().lobby;
    }

    @Override
    public int getInput() {
        int paneNr = gui.getPaneNr();
        while (true) {
            if (paneNr != -1) {
                gui.resetPaneNR();
                return paneNr;
            }
        }
    }
}
