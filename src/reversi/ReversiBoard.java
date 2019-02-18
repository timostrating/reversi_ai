package reversi;

public class ReversiBoard {

    /* Enums are classes and should follow the conventions for classes. Instances of an enum are constants and should follow the conventions for constants.https://stackoverflow.com/a/3069863 */
    public enum CellState {
        EMPTY, X, O;
        public static CellState fromNr(int nr) {return CellState.values()[nr];}
        public static CellState opponentFromNr(int nr) {return CellState.values()[nr%2+1];}
    }
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
    }

    // TODO: This accounts for all boards
    public void reset() {
        int boardSize = getBoardSize();
        for (int i=0; i<boardSize; i++)
            for (int j=0; j<boardSize; j++)
                board[i][j] = CellState.EMPTY;

        board[boardSize/2 -1][boardSize/2 -1] = CellState.fromNr(1);
        board[boardSize/2 -1][boardSize/2   ] = CellState.fromNr(2);
        board[boardSize/2   ][boardSize/2   ] = CellState.fromNr(1);
        board[boardSize/2   ][boardSize/2 -1] = CellState.fromNr(2);
    }
    public CellState[][] getBoard() { return board; }
    public int getBoardSize() { return board.length; }
    private boolean isInBounds(int x, int y) {
        return x >= 0 && x < getBoardSize() &&
               y >= 0 && y < getBoardSize();
    }

    public GameState getGameState() {
        if (!containsCell(CellState.O)) // player 2 is outplayed
            return GameState.PLAYER_1_WINS;
        if (!containsCell(CellState.X)) // player 1 is outplayed
            return GameState.PLAYER_2_WINS;

        if (!containsCell(CellState.EMPTY)) // No moves left
            return GameState.PLAYING;

        int score_X = 0;
        int score_O = 0;

        for (int y=0; y<getBoardSize(); y++) {
            for (int x = 0; x < getBoardSize(); x++) {
                if (board[y][x] == CellState.X)
                    score_X++;
                if (board[y][x] == CellState.O)
                    score_O++;
            }
        }

        if (score_X == score_O)
            return GameState.DRAW;
        if (score_X < score_O)
            return GameState.PLAYER_1_WINS;
        return GameState.PLAYER_2_WINS;
    }

    private boolean containsCell(CellState state) {
        for (int y=0; y<getBoardSize(); y++)
            for (int x=0; x<getBoardSize(); x++)
                if (board[y][x] == state)
                    return true;

        return false;
    }

    public boolean playMove(int x, int y, int playerNr) {
        if (isValidMove(x, y, playerNr)) {
            boolean flippedOpponant = false;
            for (BoardDirection dir : BoardDirection.values()) {
                int n = 1;
                boolean hasVisitedOpponent = false;
                int newX = x + n*dir.changeX;
                int newY = y + n*dir.changeY;
                while (isInBounds(newX, newY) && board[newY][newX] != CellState.EMPTY ) {
                    if (board[newY][newX] == CellState.fromNr(playerNr)) {
                        if (hasVisitedOpponent) {
                            flippedOpponant = true;
//                            System.out.printf("%s: (%d,%d) -> (%d,%d)\n", "BACKTRACK",newX, newY, x, y);
                            while (n-->1)
                                board[y + n*dir.changeY][x + n*dir.changeX] = CellState.fromNr(playerNr);
                        }
                        break;
                    } else if (board[newY][newX] == CellState.opponentFromNr(playerNr)) {
                        hasVisitedOpponent = true;
                        n++;
                        newX = x + n*dir.changeX;
                        newY = y + n*dir.changeY;
                    }
                }
            }

            if (flippedOpponant) {
                board[y][x] = CellState.fromNr(playerNr);
                return true;
            }
        }
        return false;
    }

    private boolean isValidMove(int x, int y, int playerNr) {
        if (!isInBounds(x, y) || board[y][x] != CellState.EMPTY)
            return false;

        for(BoardDirection dir : BoardDirection.values())
            if (isInBounds(x + dir.changeX, y + dir.changeY))
                if (board[y + dir.changeY][x + dir.changeX] == CellState.opponentFromNr(playerNr))
                    return true;

        return false;
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
