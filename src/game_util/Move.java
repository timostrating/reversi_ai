package game_util;

/**
 * To speedup minimax we use a command pattern. A Move is a Command in this pattern
 */
public interface Move {

    int toI();
    default void doMove() {
        doMove(true);
    }
    void doMove(boolean permanent);
    void undoMove();

}
