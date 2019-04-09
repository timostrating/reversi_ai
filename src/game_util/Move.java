package game_util;

public interface Move {

    int toI();
    void doMove(boolean permanent);
    void undoMove();

}
