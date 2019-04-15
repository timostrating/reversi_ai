package game_util;

public class GameBoard2D implements GameBoard {

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
    public int getBoardSize() {
        return boardSize;
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

    public int get(int i) {
        return board[i];
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
}