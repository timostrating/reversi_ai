package Util;

public interface Referee {

    void letTheGameStart(Callback onEnded);

    void letPlayerPlay(Player p);

    default void disqualify(Player p) {
        p.setDisqualified(true);
    }

    default GameRules.GameState getGameState() {
        return null;
    }

}
