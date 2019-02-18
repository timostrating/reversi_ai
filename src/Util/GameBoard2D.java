package Util;

public class GameBoard2D {

    int[][] board;
    int boardSize;

    public GameBoard2D(int boardSize) {
        this.boardSize = boardSize;
        this.board = new int[boardSize][boardSize];
    }

    public void reset() {
        for (int i=0; i<boardSize; i++)
            for (int j=0; j<boardSize; j++)
                board[i][j] = 0;
    }

    public int[][] getBoard() {
        return board;
    }

    public boolean containsCell(int state) {
        for (int y=0; y<boardSize; y++)
            for (int x=0; x<boardSize; x++)
                if (board[y][x] == state)
                    return true;

        return false;
    }

    public boolean isInBounds(int x, int y) {
        return x >= 0 && x < boardSize && y >= 0 && y < boardSize;
    }


    public int get(int x, int y) {
        return board[y][x];
    }

    public int set(int x, int y, int v) {
        return board[y][x] = v;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

//        sb.append("  0 1 2");
        for (int y = 0; y < boardSize; y++) {
            for (int x = 0; x < boardSize; x++)
                sb.append(board[y][x] + " ");
            sb.append("\n");
        }

        return new String(sb);
    }
}
