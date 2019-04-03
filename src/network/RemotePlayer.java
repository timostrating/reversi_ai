package network;

import util.CompositionRoot;
import game_util.Player;

public class RemotePlayer extends Player {
    Connection connection;

    public RemotePlayer() {
        connection = CompositionRoot.getInstance().connection;
    }

    @Override
    public int getInput() {
        //TODO fixen
        return 0;
    }
}
