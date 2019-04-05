package reversi;

import game_util.GameBoard2D;
import game_util.GameRules;
import util.OpenPositions;

import java.util.LinkedList;

public class Reversi extends GameRules {

    public static final int BOARD_SIZE = 8;
    public static final int CELL_COUNT = BOARD_SIZE * BOARD_SIZE;

//    private OpenPositionsReversi[] openPositions = new OpenPositionsReversi[]{ TODO: support it for 2 players
//        new OpenPositionsReversi(),
//        new OpenPositionsReversi()
//    };
    private OpenPositionsReversi openPositions = new OpenPositionsReversi();


    public GameBoard2D board;

    public Reversi() {
        this.board = new GameBoard2D(BOARD_SIZE);
        board.reset(this::reset);
    }

    public void reset() {
        openPositions.reset();
        setOnBoardAndNotify(BOARD_SIZE / 2 - 1, BOARD_SIZE / 2 - 1, CellState.X.ordinal());
        setOnBoardAndNotify(BOARD_SIZE / 2 - 1, BOARD_SIZE / 2,     CellState.O.ordinal());
        setOnBoardAndNotify(BOARD_SIZE / 2    , BOARD_SIZE / 2,     CellState.X.ordinal());
        setOnBoardAndNotify(BOARD_SIZE / 2    , BOARD_SIZE / 2 - 1, CellState.O.ordinal());
    }

    public OpenPositionsReversi getOpenPositions(int playerNr) {
        return openPositions; // [playerNr -1] TODO support it for 2 players
    }

    private void setOnBoardAndNotify(int x, int y, int playerNr) {
        board.set(x,y,playerNr);
        openPositions.remove(new Integer(board.xyToI(x, y)));
        for (BoardDirection dir : BoardDirection.values()) {
            int n = 0;
            int newX = x + n * dir.changeX;
            int newY = y + n * dir.changeY;
            while (board.isInBounds(newX, newY) && board.get(newY, newX) != CellState.X.ordinal()) {  // TODO support it for 2 players
                if (board.get(newY, newX) == CellState.EMPTY.ordinal()) {
                    int i = board.xyToI(newX, newY);
                    if (isValidMove(i, CellState.X.ordinal())) // TODO Possibly already in the openPositions
                        openPositions.add(i);
                    break;
                } else {
                    n++;
                    newX = x + n * dir.changeX;
                    newY = y + n * dir.changeY;
                }
            }
        }
    }

    private void flipOnBoardAndNotify(int x, int y, int playerNr) {
        board.set(x,y,playerNr);

        if (playerNr == CellState.O.ordinal()) { // TODO support it for 2 players
            for (BoardDirection dir : BoardDirection.values()) {
                int i = board.xyToI(x + dir.changeX, y + dir.changeY);
                if (isValidMove(i, CellState.X.ordinal())) // TODO Possibly already in the openPositions
                    openPositions.add(i);
            }
        }
    }

    public GameState getGameSpecificState() {
        if (!board.containsCell(CellState.O.ordinal())) // player 2 is outplayed
            return GameState.PLAYER_1_WINS;
        if (!board.containsCell(CellState.X.ordinal())) // player 1 is outplayed
            return GameState.PLAYER_2_WINS;

        if (board.containsCell(CellState.EMPTY.ordinal())) // No moves left
            return GameState.PLAYING;

        int score_X = 0;
        int score_O = 0;

        for (int y = 0; y < BOARD_SIZE; y++) {
            for (int x = 0; x < BOARD_SIZE; x++) {
                if (board.get(y, x) == CellState.X.ordinal())
                    score_X++;
                if (board.get(y, x) == CellState.O.ordinal())
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
        boolean validPlayMove = playOrTestMove(board.iToX(i), board.iToY(i), playerNr, false);

        if (validPlayMove)
            onValidMovePlayed.notifyObjects(o -> o.callback(i));

        return validPlayMove;
    }

    public boolean isValidMove(int i, int playerNr) {
        return playOrTestMove(board.iToX(i), board.iToY(i), playerNr, true);
    }

    private boolean playOrTestMove(int x, int y, int playerNr, boolean testForValidMove) {
        if (!touchesOpponentAndIsEmpty(x, y, playerNr))
            return false;

        boolean flippedOpponent = false;
        for (BoardDirection dir : BoardDirection.values()) {
            int n = 1;
            boolean hasVisitedOpponent = false;
            int newX = x + n * dir.changeX;
            int newY = y + n * dir.changeY;
            while (board.isInBounds(newX, newY) && board.get(newY, newX) != CellState.EMPTY.ordinal()) {
                if (board.get(newY, newX) == CellState.fromNr(playerNr).ordinal()) {
                    if (hasVisitedOpponent) {
                        if (testForValidMove) // isValidMove()
                            return true;

                        flippedOpponent = true;
//                            System.out.printf("%s: (%d,%d) -> (%d,%d)\n", "BACKTRACK",newX, newY, x, y);
                        while (n-- > 1)
                            flipOnBoardAndNotify(y + n * dir.changeY, x + n * dir.changeX, CellState.fromNr(playerNr).ordinal());

                    }
                    break;
                } else if (board.get(newY, newX) == CellState.opponentFromNr(playerNr).ordinal()) {
                    hasVisitedOpponent = true;
                    n++;
                    newX = x + n * dir.changeX;
                    newY = y + n * dir.changeY;
                }
            }
        }

        if (flippedOpponent && !testForValidMove) { // set move only if we are not testing
            setOnBoardAndNotify(y, x, CellState.fromNr(playerNr).ordinal());
            return true;
        }

        return false;
    }


    private boolean touchesOpponentAndIsEmpty(int x, int y, int playerNr) {
        if (!board.isInBounds(x, y) || board.get(y, x) != CellState.EMPTY.ordinal())
            return false;

        for (BoardDirection dir : BoardDirection.values())
            if (board.isInBounds(x + dir.changeX, y + dir.changeY))
                if (board.get(y + dir.changeY, x + dir.changeX) == CellState.opponentFromNr(playerNr).ordinal())
                    return true;

        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("  0 1 2 3 4 5 6 7"); // TODO: this is hardcoded
        for (int y = 0; y < BOARD_SIZE; y++) {
            sb.append("\n" + y + " ");
            for (int x = 0; x < BOARD_SIZE; x++)
                sb.append((board.get(y, x) == CellState.EMPTY.ordinal()) ? "- " : CellState.values()[board.get(y, x)] + " ");
        }

        return new String(sb);
    }


    /* Enums are classes and should follow the conventions for classes. Instances of an enum are constants and should follow the conventions for constants.https://stackoverflow.com/a/3069863 */
    public enum CellState {
        EMPTY, X, O;

        public static CellState fromNr(int nr) {
            return CellState.values()[nr];
        }

        public static CellState opponentFromNr(int nr) {
            return CellState.values()[nr % 2 + 1];
        }
    }

    public enum BoardDirection {
        LEFT_UP(-1, 1),     UP(0, 1),     RIGHT_UP(1, 1),
        LEFT(-1, 0),                      RIGHT(1, 0),
        LEFT_DOWN(-1, -1),  DOWN(0, -1),  RIGHT_DOWN(1, -1);

        public final int changeX;
        public final int changeY;

        BoardDirection(int changeX, int changeY) {
            this.changeX = changeX;
            this.changeY = changeY;
        }
    }

    protected class OpenPositionsReversi implements OpenPositions { // TODO we should implement this using a different data structure

        LinkedList<Integer> list = new LinkedList<>();

        void reset() {
            list.add(board.xyToI(2, 4));
            list.add(board.xyToI(5, 3));
            list.add(board.xyToI(4, 2));
            list.add(board.xyToI(3, 5));

            //    0 1 2 3 4 5 6 7
            //  0 - - - - - - - -
            //  1 - - - - - - - -
            //  2 - - - - _ - - -
            //  3 - - - X O _ - -
            //  4 - - _ O X - - -
            //  5 - - - _ - - - -
            //  6 - - - - - - - -
            //  7 - - - - - - - -
        }

        public int size() { return list.size(); }
        public Integer get(int posIndex) { return list.get(posIndex); }

        public Integer remove(int posIndex) {
            return list.remove(posIndex);
        }

        public boolean remove(Object pos) {
            return list.remove(pos);
        }

        @Override
        public boolean add(Integer pos) {
            if (!list.contains(pos)) // TODO really slow
                list.add(pos);

            return true;
        }

        @Override
        public void add(int posIndex, Integer pos) {
            if (!list.get(posIndex - 1).equals(pos) && !list.get(posIndex).equals(pos) && !list.get(posIndex + 1).equals(pos)) // TODO really slow
                list.add(posIndex, pos);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (int i : list) {
                sb.append("("+board.iToX(i) +" "+ board.iToY(i) + "), ");
            }
            return sb.toString();
        }
    }
}
