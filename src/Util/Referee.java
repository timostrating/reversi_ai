package Util;

public interface Referee {

    void letTheGameStart();

    void letPlayerPlay(Player p);

    default void disqualify(Player p) {
        p.setDisqualified(true);
    }

}
