package netwerk;

import Util.CompositionRoot;
import Util.Player;

public class RemotePlayer extends Player {
    Connection connection;

    public RemotePlayer() {
        connection = CompositionRoot.getInstance().connection;
    }

    @Override
    public int getInput() {
        return connection.getMove();
    }
}
