package game_util;

import util.Callback;

public interface Referee {

    default void initialize() {};

    void letTheGameStart(Callback onEnded);

    void letPlayerPlay(Player p);

    default void disqualify(Player p) {
        p.setDisqualified(true);
    }

    default GameRules.GameState getGameState() {
        return null;
    }

}
