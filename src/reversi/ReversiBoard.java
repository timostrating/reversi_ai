package reversi;

public class ReversiBoard {

    /* Enums are classes and should follow the conventions for classes. Instances of an enum are constants and should follow the conventions for constants.https://stackoverflow.com/a/3069863 */
    public enum CellState {EMPTY, X, O; public static CellState fromNr(int nr) {return CellState.values()[nr];} }
    public enum GameState {PLAYING, DRAW, PLAYER_1_WINS, PLAYER_2_WINS}
    public enum BoardDirection {
        LEFT_UP  (-1,  1),  UP  (0,  1),    RIGHT_UP  (1,  1),
        LEFT     (-1,  0),                  RIGHT     (1,  0),
        LEFT_DOWN(-1, -1),  DOWN(0, -1),    RIGHT_DOWN(1, -1);

        public final int changeX;
        public final int changeY;

        BoardDirection(int changeX, int changeY) {
            this.changeX = changeX;
            this.changeY = changeY;
        }
    }
    private CellState[][] board;

    public ReversiBoard(int boardSize) {
        board = new CellState[boardSize][boardSize];
        this.reset();
        board[boardSize/2 -1][boardSize/2 -1] = CellState.fromNr(1);
        board[boardSize/2 -1][boardSize/2   ] = CellState.fromNr(2);
        board[boardSize/2   ][boardSize/2   ] = CellState.fromNr(1);
        board[boardSize/2   ][boardSize/2 -1] = CellState.fromNr(2);
    }

    // TODO: This accounts for all boards
    public void reset() {
        for (int i=0; i<getBoardSize(); i++)
            for (int j=0; j<getBoardSize(); j++)
                board[i][j] = CellState.EMPTY;
    }

    public GameState getGameState() {
        return GameState.PLAYING; // TODO:
    }


    public CellState[][] getBoard() { return board; } // TODO:
    public int getBoardSize() { return board.length; }


    public boolean playMove(int x, int y, int playerNr) {
        if (isValidMove(x, y, playerNr)) {
            board[y][x] = CellState.fromNr(playerNr);
            // TODO: ...
            return true;
        }
        return false;
    }

    private boolean isValidMove(int x, int y, int playerNr) {
        if (!isInBounds(x, y) || board[y][x] != CellState.EMPTY)
            return false;

        for(BoardDirection pos : BoardDirection.values())
            if (isInBounds(x + pos.changeX, y + pos.changeY))
                if (board[y + pos.changeY][x + pos.changeX] == CellState.fromNr(playerNr%2+1))
                    return true;

        return false;
    }
    private boolean isInBounds(int x, int y) {
        return x >= 0 && x < getBoardSize() &&
               y >= 0 && y < getBoardSize();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("  0 1 2 3 4 5 6 7"); // TODO: this is hardcoded
        for (int y=0; y<getBoardSize(); y++) {
            sb.append("\n" +y+ " ");
            for (int x=0; x<getBoardSize(); x++)
                sb.append((board[y][x] == CellState.EMPTY)? "- " : board[y][x].name()+" ");
        }

        return new String(sb);
    }
}
