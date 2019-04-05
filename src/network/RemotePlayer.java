package network;

import game_util.Player;

public class RemotePlayer extends Player {

    @Override
    public int getInput() {
        return -1; // RemotePlayer is not responsible for getting remote input. (The NetworkedReferee does that)
    }
}
