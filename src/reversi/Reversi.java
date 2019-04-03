package reversi;

import game_util.GameBoard2D;
import game_util.GameRules;

public class Reversi extends GameRules {

    public static final int BOARD_SIZE = 8;

    /* Enums are classes and should follow the conventions for classes. Instances of an enum are constants and should follow the conventions for constants.https://stackoverflow.com/a/3069863 */
    public enum CellState {
        EMPTY, X, O;
        public static CellState fromNr(int nr) {return CellState.values()[nr];}
        public static CellState opponentFromNr(int nr) {return CellState.values()[nr%2+1];}
    }
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

    public GameBoard2D board;

    public Reversi() {
        this.board = new GameBoard2D(BOARD_SIZE);
        board.reset(this::reset);
    }

    public void reset() {
        board.set(BOARD_SIZE/2 -1, BOARD_SIZE/2 -1, CellState.X.ordinal());
        board.set(BOARD_SIZE/2 -1, BOARD_SIZE/2   , CellState.O.ordinal());
        board.set(BOARD_SIZE/2   , BOARD_SIZE/2   , CellState.X.ordinal());
        board.set(BOARD_SIZE/2   , BOARD_SIZE/2 -1, CellState.O.ordinal());
    }

    public GameState getGameState() {
        if (!board.containsCell(CellState.O.ordinal())) // player 2 is outplayed
            return GameState.PLAYER_1_WINS;
        if (!board.containsCell(CellState.X.ordinal())) // player 1 is outplayed
            return GameState.PLAYER_2_WINS;

        if (board.containsCell(CellState.EMPTY.ordinal())) // No moves left
            return GameState.PLAYING;

        int score_X = 0;
        int score_O = 0;

        for (int y=0; y<BOARD_SIZE; y++) {
            for (int x = 0; x < BOARD_SIZE; x++) {
                if (board.get(y,x) == CellState.X.ordinal())
                    score_X++;
                if (board.get(y,x) == CellState.O.ordinal())
                    score_O++;
            }
        }

        if (score_X == score_O)
            return GameState.DRAW;
        if (score_X < score_O)
            return GameState.PLAYER_1_WINS;
        return GameState.PLAYER_2_WINS;
    }

    public boolean playMove(int i, int playerNr) {
        int x = i % BOARD_SIZE;
        int y = i / BOARD_SIZE;
        return playMove(x, y, playerNr);
    }
    public boolean playMove(int x, int y, int playerNr) {
        if (isPossibleValidMove(x, y, playerNr)) {
            boolean flippedOpponent = false;
            for (BoardDirection dir : BoardDirection.values()) {
                int n = 1;
                boolean hasVisitedOpponent = false;
                int newX = x + n*dir.changeX;
                int newY = y + n*dir.changeY;
                while (board.isInBounds(newX, newY) && board.get(newY,newX) != CellState.EMPTY.ordinal() ) {
                    if (board.get(newY,newX) == CellState.fromNr(playerNr).ordinal()) {
                        if (hasVisitedOpponent) {
                            flippedOpponent = true;
//                            System.out.printf("%s: (%d,%d) -> (%d,%d)\n", "BACKTRACK",newX, newY, x, y);
                            while (n-->1)
                                board.set(y + n*dir.changeY,x + n*dir.changeX, CellState.fromNr(playerNr).ordinal());
                        }
                        break;
                    } else if (board.get(newY,newX) == CellState.opponentFromNr(playerNr).ordinal()) {
                        hasVisitedOpponent = true;
                        n++;
                        newX = x + n*dir.changeX;
                        newY = y + n*dir.changeY;
                    }
                }
            }

            if (flippedOpponent) {
                board.set(y,x, CellState.fromNr(playerNr).ordinal());
                return true;
            }
        }
        return false;
    }

    private boolean isPossibleValidMove(int x, int y, int playerNr) {
        if (!board.isInBounds(x, y) || board.get(y,x) != CellState.EMPTY.ordinal())
            return false;

        for(BoardDirection dir : BoardDirection.values())
            if (board.isInBounds(x + dir.changeX, y + dir.changeY))
                if (board.get(y + dir.changeY,x + dir.changeX) == CellState.opponentFromNr(playerNr).ordinal())
                    return true;

        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("  0 1 2 3 4 5 6 7"); // TODO: this is hardcoded
        for (int y=0; y<BOARD_SIZE; y++) {
            sb.append("\n" +y+ " ");
            for (int x=0; x<BOARD_SIZE; x++)
                sb.append((board.get(y,x) == CellState.EMPTY.ordinal())? "- " : CellState.values()[board.get(y,x)]+" ");
        }

        return new String(sb);
    }
}
