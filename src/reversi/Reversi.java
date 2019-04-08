package reversi;

import game_util.GameBoard2D;
import game_util.GameRules;
import game_util.Player;
import util.OpenPosition;
import util.OpenPositions;
import util.Utils;

import java.util.LinkedList;

public class Reversi extends GameRules {

    public static final int BOARD_SIZE = 8;
    public static final int CELL_COUNT = BOARD_SIZE * BOARD_SIZE;

    private OpenPositionsReversi openPositions = new OpenPositionsReversi();

    public GameBoard2D board;

    public Reversi() {
        this.board = new GameBoard2D(BOARD_SIZE);
        board.reset(this::reset);
    }

    public void reset() {
        setOnBoardAndNotify(BOARD_SIZE / 2 - 1, BOARD_SIZE / 2 - 1, CellState.X.ordinal());
        setOnBoardAndNotify(BOARD_SIZE / 2 - 1, BOARD_SIZE / 2, CellState.O.ordinal());
        setOnBoardAndNotify(BOARD_SIZE / 2, BOARD_SIZE / 2, CellState.X.ordinal());
        setOnBoardAndNotify(BOARD_SIZE / 2, BOARD_SIZE / 2 - 1, CellState.O.ordinal());
    }

    public OpenPositionsReversi getOpenPositions() {
        return openPositions;
    }

    private void setOnBoardAndNotify(int x, int y, int playerNr) {
        board.set(x, y, playerNr);
        openPositions.onChange(x, y);
    }

    private void flipOnBoardAndNotify(int x, int y, int playerNr) {
        board.set(x, y, playerNr);
        openPositions.onChange(x, y);
    }

    @Override
    protected boolean canPlay(Player p) {
        return openPositions.getOpenPositions(p.getNr()).size() > 0;
    }

    public GameState getGameSpecificState() {
        if (!board.containsCell(CellState.O.ordinal())) // player 2 is outplayed
            return GameState.PLAYER_1_WINS;
        if (!board.containsCell(CellState.X.ordinal())) // player 1 is outplayed
            return GameState.PLAYER_2_WINS;

        boolean canMove = openPositions.openOPositions.size() > 0 || openPositions.openXPositions.size() > 0;

        if (canMove && board.containsCell(CellState.EMPTY.ordinal()))
            return GameState.PLAYING;

        int score_X = 0;
        int score_O = 0;

        for (int y = 0; y < BOARD_SIZE; y++) {
            for (int x = 0; x < BOARD_SIZE; x++) {
                if (board.get(x, y) == CellState.X.ordinal())
                    score_X++;
                if (board.get(x, y) == CellState.O.ordinal())
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
            while (board.isInBounds(newX, newY) && board.get(newX, newY) != CellState.EMPTY.ordinal()) {
                if (board.get(newX, newY) == CellState.fromNr(playerNr).ordinal()) {
                    if (hasVisitedOpponent) {
                        if (testForValidMove) // isValidMove()
                            return true;

                        flippedOpponent = true;
//                            System.out.printf("%s: (%d,%d) -> (%d,%d)\n", "BACKTRACK",newX, newY, x, y);
                        while (n-- > 1)
                            flipOnBoardAndNotify(x + n * dir.changeX, y + n * dir.changeY, CellState.fromNr(playerNr).ordinal());

                    }
                    break;
                } else if (board.get(newX, newY) == CellState.opponentFromNr(playerNr).ordinal()) {
                    hasVisitedOpponent = true;
                    n++;
                    newX = x + n * dir.changeX;
                    newY = y + n * dir.changeY;
                }
            }
        }

        if (flippedOpponent && !testForValidMove) { // set move only if we are not testing
            setOnBoardAndNotify(x, y, CellState.fromNr(playerNr).ordinal());
            return true;
        }

        return false;
    }


    private boolean touchesOpponentAndIsEmpty(int x, int y, int playerNr) {
        if (!board.isInBounds(x, y) || board.get(x, y) != CellState.EMPTY.ordinal())
            return false;

        for (BoardDirection dir : BoardDirection.values())
            if (board.isInBounds(x + dir.changeX, y + dir.changeY))
                if (board.get(x + dir.changeX, y + dir.changeY) == CellState.opponentFromNr(playerNr).ordinal())
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
                sb.append((board.get(x, y) == CellState.EMPTY.ordinal()) ? "- " : CellState.values()[board.get(x, y)] + " ");
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
        LEFT_UP(-1, -1),     UP(0, -1),    RIGHT_UP(1, -1),
        LEFT(-1, 0),                     RIGHT(1, 0),
        LEFT_DOWN(-1, 1),  DOWN(0, 1), RIGHT_DOWN(1, 1);

        public final int changeX;
        public final int changeY;

        BoardDirection(int changeX, int changeY) {
            this.changeX = changeX;
            this.changeY = changeY;
        }

        public int stepsToBorder(int x, int y) {
            switch (this){
                case UP: return y;
                case RIGHT: return Reversi.BOARD_SIZE - x -1;
                case DOWN: return Reversi.BOARD_SIZE - y -1;
                case LEFT: return x;

                case RIGHT_UP: return Math.min(UP.stepsToBorder(x, y), RIGHT.stepsToBorder(x, y));
                case RIGHT_DOWN: return Math.min(DOWN.stepsToBorder(x, y), RIGHT.stepsToBorder(x, y));
                case LEFT_UP: return Math.min(UP.stepsToBorder(x, y), LEFT.stepsToBorder(x, y));
                case LEFT_DOWN: return Math.min(DOWN.stepsToBorder(x, y), LEFT.stepsToBorder(x, y));
            }
            throw new RuntimeException("Nice");
        }

        public int xStepsToBorder(int x, int y) {
            int steps = stepsToBorder(x, y);
            switch (this){
                case UP:
                case DOWN: return 0;

                case RIGHT_UP:
                case RIGHT_DOWN:
                case RIGHT: return steps;

                case LEFT_UP:
                case LEFT_DOWN:
                case LEFT: return -steps;
            }
            throw new RuntimeException("New to AliExpress?\n" +
                    "Join us today and we'll give you up to $3 in coupons");
        }

        public int yStepsToBorder(int x, int y) {
            int steps = stepsToBorder(x, y);
            switch (this){
                case LEFT_UP:
                case RIGHT_UP:
                case UP: return -steps;

                case RIGHT_DOWN:
                case LEFT_DOWN:
                case DOWN: return steps;

                case LEFT:
                case RIGHT: return 0;
            }
            throw new RuntimeException("New to AliExpress?\n" +
                    "Join us today and we'll give you up to $3 in coupons");
        }
    }

    protected class OpenPositionsReversi implements OpenPositions<OpenPosition> { // TODO we should implement this using a different data structure }

        LinkedList<OpenPosition>
                openXPositions = new LinkedList<>(),
                openOPositions = new LinkedList<>();

        boolean[][][]
                validationArrowsX = new boolean[BOARD_SIZE][BOARD_SIZE][BoardDirection.values().length],
                validationArrowsO = new boolean[BOARD_SIZE][BOARD_SIZE][BoardDirection.values().length];


        public void onChange(int x, int y) {
            for (BoardDirection dir : BoardDirection.values()) {
                checkOpenPositionsInLine(x + dir.xStepsToBorder(x, y), y + dir.yStepsToBorder(x, y), dir);
            }
        }

        private void addArrow(int x, int y, int playerNr, BoardDirection dir) {
            boolean[][][] arrows = playerNr == 1 ? validationArrowsX : validationArrowsO;

            if (!Utils.any(arrows[x][y]))
                getOpenPositions(playerNr).add(new OpenPosition(board.xyToI(x, y)));

            arrows[x][y][dir.ordinal()] = true;
        }

        private void removeArrow(int x, int y, int playerNr, BoardDirection dir) {
            boolean[][][] arrows = playerNr == 1 ? validationArrowsX : validationArrowsO;

            if (!arrows[x][y][dir.ordinal()]) return;

            arrows[x][y][dir.ordinal()] = false;
            if (!Utils.any(arrows[x][y])) {
                // remove openPosition
                filter(board.xyToI(x, y), playerNr);
            }
        }

        private void checkOpenPositionsInLine(int x, int y, BoardDirection dir) {

            int hasSeenX = -1, hasSeenO = -1;
            int i = 0;
            while (board.isInBounds(x, y)) {

                if (board.get(x, y) ==  CellState.X.ordinal())
                    hasSeenX = i;
                if (board.get(x, y) ==  CellState.O.ordinal())
                    hasSeenO = i;
                if (board.get(x, y) ==  CellState.EMPTY.ordinal()) {

                    boolean addArrow =hasSeenX >= 0 && hasSeenO >= 0;

                    if (hasSeenX < hasSeenO && addArrow) {
                        addArrow(x,y, CellState.X.ordinal(), dir);
                    } else {
                        removeArrow(x,y, CellState.X.ordinal(), dir);
                    }

                    if (hasSeenX > hasSeenO && addArrow) {
                        addArrow(x,y, CellState.O.ordinal(), dir);
                    } else {
                        removeArrow(x,y, CellState.O.ordinal(), dir);
                    }

                    hasSeenX = -1;
                    hasSeenO = -1;
                }

                x -= dir.changeX;
                y -= dir.changeY;
                i++;
            }

        }


        LinkedList<OpenPosition> getOpenPositions(int playerNr) {
            return playerNr == 1 ? openXPositions : openOPositions;
        }

        @Override
        public int size(int playerNr) { return getOpenPositions(playerNr).size(); }

        @Override
        public OpenPosition get(int posIndex, int playerNr) { return getOpenPositions(playerNr).get(posIndex); }

        @Override
        public OpenPosition remove(int posIndex, int playerNr) { return getOpenPositions(playerNr).remove(posIndex); }

        @Override
        public void add(int posIndex, OpenPosition pos, int playerNr) { getOpenPositions(playerNr).add(posIndex, pos); }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append( "__X__\n");
            for (OpenPosition op : openXPositions)
                sb.append("(").append(board.iToX(op.i)).append(" ").append(board.iToY(op.i)).append("), ");
            sb.append( "\n__O__\n");
            for (OpenPosition op : openOPositions)
                sb.append("(").append(board.iToX(op.i)).append(" ").append(board.iToY(op.i)).append("), ");

            return sb.toString();
        }
    }
}
