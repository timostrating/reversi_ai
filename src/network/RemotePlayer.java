package network;

import game_util.Move;
import game_util.Player;

/**
 * When you play with an other player online you play against this remote player.
 * The RemotePlayer is not responsible for getting remote input. (The NetworkedReferee does that)
 * The implementation of the networking protocol of the server is not optimal so in order to get it working we make the NetworkedReferee get the input for this player.
 */
public class RemotePlayer extends Player {

    @Override
    public Move getInput() {
        return null; // The NetworkedReferee gets the move
    }
}
