package game_util;

import util.Callback;

public interface GameBoard {

    int getBoardSize();

    GameBoard clone();

    default int xyToI(int x, int y) {return y * getBoardSize() + x; }

    default int iToX(int i) {return i % getBoardSize(); }

    default int iToY(int i) {return i / getBoardSize(); }

    default boolean isInBounds(int i) {return i >= 0 && i < getBoardSize() * getBoardSize(); }

    default boolean isInBounds(int x, int y) {return x >= 0 && x < getBoardSize() && y >= 0 && y < getBoardSize(); }

    default int amount(int playerNr) {  // TODO this is slow. there are faster ways to achieve the same result
        int n = 0;
        for (int i=0; i<getBoardSize()*getBoardSize(); i++)
            if (get(i) == playerNr)
                n++;
        return n;
    }

    default int boardMlScore() {
        int n[] = new int[3];

        for (int i=0; i<getBoardSize()*getBoardSize(); i++)
            n[get(i)] += 1;

        if (n[0] > n[1]) // more 0 than 1
            if (n[0] > n[2]) // more 0 than 2
                return 0;
        return Integer.compare(n[1], n[2]);
    }

    void reset();
    default void reset(Callback onReset) {
        reset();
        onReset.callback();
    }

    default int get(int x, int y) { return get(xyToI(x, y)); }
    int get(int i);

    default void set(int x, int y, int v) { set(xyToI(x, y), v); }
    void set(int i, int v);
}