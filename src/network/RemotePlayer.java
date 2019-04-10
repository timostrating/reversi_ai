package network;

import game_util.Move;
import game_util.Player;

public class RemotePlayer extends Player {

    @Override
    public Move getInput() {
        return null; // RemotePlayer is not responsible for getting remote input. (The NetworkedReferee does that)
    }
}
