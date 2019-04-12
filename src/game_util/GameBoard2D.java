package game_util;

import util.Callback;
import util.EnormousInt;

import java.math.BigInteger;

public class GameBoard2D {

    int[] board;
    int boardSize;
    int cellCount;

    public GameBoard2D(int boardSize) {
        this.boardSize = boardSize;
        this.cellCount = boardSize * boardSize;
        this.board = new int[cellCount];
    }

    public GameBoard2D clone() {
        GameBoard2D newBoard = new GameBoard2D(boardSize);
        newBoard.board = board.clone();
        return newBoard;
    }

    public void reset(Callback onReset) {
        reset();
        onReset.callback();
    }

    public void reset() {
        for (int i = 0; i < cellCount; i++)
            board[i] = 0;
    }

    public int[] getBoard() {
        return board;
    }

    public boolean containsCell(int state) {
        for (int i = 0; i < cellCount; i++)
            if (board[i] == state)
                return true;

        return false;
    }

    public int xyToI(int x, int y) {
        return y * boardSize + x;
    }

    public int iToX(int i) {
        return i % boardSize;
    }

    public int iToY(int i) {
        return i / boardSize;
    }

    public boolean isInBounds(int i) {
        return i >= 0 && i < cellCount;
    }

    public boolean isInBounds(int x, int y) {
        return x >= 0 && x < boardSize && y >= 0 && y < boardSize;
    }


    public int get(int x, int y) {
        return board[y * boardSize + x];
    }

    public int get(int i) {
        return board[i];
    }

    public void set(int x, int y, int v) {
        board[y * boardSize + x] = v;
    }

    public void set(int i, int v) {
        board[i] = v;
    }

    public int amount(int v) {
        int amount = 0;
        for (int a : board) if (a == v) amount++;
        return amount;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

//        sb.append("  0 1 2");
        for (int y = 0; y < boardSize; y++) {
            for (int x = 0; x < boardSize; x++)
                sb.append(board[y * boardSize + x]).append(" ");
            sb.append("\n");
        }

        return new String(sb);
    }

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
}