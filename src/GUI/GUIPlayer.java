package GUI;

import Util.Player;
import static GUI.Lobby.getPaneNr;
import static GUI.Lobby.setPaneNR;

public class GUIPlayer implements Player {
    @Override
    public int getNr() {
        return 2;
    }

    @Override
    public int getInput() {
        int paneNr = getPaneNr();
        while (true) {
            if (paneNr != -1) {
                setPaneNR(-1, 0);
                return paneNr;
            }
        }
    }
}
