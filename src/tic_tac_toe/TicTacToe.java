package tic_tac_toe;

import game_util.GameBoard2D;
import game_util.GameRules;
import javafx.util.Pair;

import static game_util.BoardHelper.areAllEqual;

public class TicTacToe extends GameRules {

    public static final int BOARD_SIZE = 3;

    /* Enums are classes and should follow the conventions for classes. Instances of an enum are constants and should follow the conventions for constants.https://stackoverflow.com/a/3069863 */
    public enum CellState {EMPTY, X, O}
    public GameBoard2D board;

    public TicTacToe() {
        board = new GameBoard2D(TicTacToe.BOARD_SIZE);
        board.reset();
    }

    public GameState getGameSpecificState() {
        if(matchOf3(CellState.X))
            return GameState.PLAYER_1_WINS;
        if(matchOf3(CellState.O))
            return GameState.PLAYER_2_WINS;

        if (board.containsCell(CellState.EMPTY.ordinal()))
            return GameState.PLAYING;

        return GameState.DRAW;
    }

    private boolean matchOf3(CellState player) {
        return  areAllEqual(player.ordinal(), board.get(0,0), board.get(1,0), board.get(2,0)) ||
                areAllEqual(player.ordinal(), board.get(0,1), board.get(1,1), board.get(2,1)) ||
                areAllEqual(player.ordinal(), board.get(0,2), board.get(1,2), board.get(2,2)) ||
                areAllEqual(player.ordinal(), board.get(0,0), board.get(0,1), board.get(0,2)) ||
                areAllEqual(player.ordinal(), board.get(1,0), board.get(1,1), board.get(1,2)) ||
                areAllEqual(player.ordinal(), board.get(2,0), board.get(2,1), board.get(2,2)) ||
                areAllEqual(player.ordinal(), board.get(0,0), board.get(1,1), board.get(2,2)) ||
                areAllEqual(player.ordinal(), board.get(2,0), board.get(1,1), board.get(0,2));
    }

    public boolean playMove(int i, int playerNr) {
        if (isValidMove(i) && getGameSpecificState() == GameState.PLAYING) {
            board.set(i, CellState.values()[playerNr].ordinal());
            onValidMovePlayed.notifyObjects(o -> o.callback(new Pair<>(i, playerNr)));
            return true;
        }

        return false;
    }

    private boolean isValidMove(int i) {
        return board.isInBounds(i) && board.get(i) == CellState.EMPTY.ordinal();
    }

    @Override
    public String toString() {
        return board.toString().replace("0", "-").replace("1", "X").replace("2","O");
    }

}
