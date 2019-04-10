package game_util;

public interface Move {

    int toI();
    default void doMove() {
        doMove(true);
    }
    void doMove(boolean permanent);
    void undoMove();

}
