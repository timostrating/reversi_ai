package Util;

public class GameBoard2D {

    int[] board;
    int boardSize;
    int cellCount;

    Delegate<CallbackWithParam<Integer>> onSetPlayed = new Delegate<>();

    public GameBoard2D(int boardSize) {
        this.boardSize = boardSize;
        this.cellCount = boardSize * boardSize;
        this.board = new int[cellCount];
    }

    public void reset() {
        for (int i=0; i<cellCount; i++)
            board[i] = 0;
    }

    public int[] getBoard() {
        return board;
    }

    public boolean containsCell(int state) {
        for (int i=0; i<cellCount; i++)
            if (board[i] == state)
                return true;

        return false;
    }

    public boolean isInBounds(int i) { return i < cellCount; }
    public boolean isInBounds(int x, int y) { return x >= 0 && x < boardSize && y >= 0 && y < boardSize; }


    public int get(int x, int y) { return board[y * boardSize + x]; }
    public int get(int i) { return board[i]; }

    public void set(int i, int v) {
        board[i] = v;
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
}
