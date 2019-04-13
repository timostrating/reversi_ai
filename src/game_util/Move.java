package game_util;

public interface Move {

    int toI();
    int playerNr();
    default void doMove() {
        doMove(true);
    }
    void doMove(boolean permanent);
    void undoMove();

}
