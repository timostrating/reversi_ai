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

    void reset();
    default void reset(Callback onReset) {
        reset();
        onReset.callback();
    }

    default int get(int x, int y) { return get(xyToI(x, y)); }
    int get(int i);

    default void set(int x, int y, int v) { set(xyToI(x, y), v); }
    void set(int i, int v);

    /*
    @Override
    public boolean equals(Object o) {
        throw new RuntimeException("You are not allowed to compare Boards with equals");
    }

    @Override
    public int hashCode() {
        return Integer.parseInt(hashBoard());
    }

    private String hashBoard() {
        BigInteger score = new EnormousInt(0);

        for (int i = 0; i < cellCount; i++) {
            score = score.add(new EnormousInt(board[i]).multiply(new EnormousInt(3).pow(i)));
        }

        System.err.println(Integer.MAX_VALUE);
        score = score.mod(EnormousInt.MAX_PRIME_LESS_THAN_INTEGER_MAXVALUE);

        return score.toString();
    }
     */
}
