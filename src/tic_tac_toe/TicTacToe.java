package tic_tac_toe;

import game_util.GameBoard2D;
import game_util.GameRules;
import javafx.util.Pair;
import game_util.Move;
import util.OpenPositions;

import java.util.LinkedList;

import static game_util.BoardHelper.areAllEqual;

public class TicTacToe extends GameRules {

    public static final int BOARD_SIZE = 3;

    /* Enums are classes and should follow the conventions for classes. Instances of an enum are constants and should follow the conventions for constants.https://stackoverflow.com/a/3069863 */
    public enum CellState {EMPTY, X, O}
    public GameBoard2D board;
    public LinkedListOpenPositionsWrapper openPositions = new LinkedListOpenPositionsWrapper();

    public TicTacToe() {
        board = new GameBoard2D(TicTacToe.BOARD_SIZE);
        board.reset();
        for (int i=0; i <= 8; i++)
            openPositions.add(i);
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


    public Move getMove(int i, int playerNr) {
        if (isValidMove(i) && getGameSpecificState() == GameState.PLAYING) {
            return new Move() {
                @Override
                public int toI() { return i; }

                @Override
                public int playerNr() {
                    return playerNr;
                }

                @Override
                public void doMove(boolean permanent) {
                    board.set(i, CellState.values()[playerNr].ordinal());
                    openPositions.filter(i, playerNr);
                    if (permanent) {
                        onPermanentMovePlayed.notifyObjects(o -> o.callback(this));
                    }
                }

                @Override
                public void undoMove() {
                    board.set(i, 0);
                    openPositions.add(i);
                }
            };
        }
        return null;
    }

    private boolean isValidMove(int i) {
        return board.isInBounds(i) && board.get(i) == CellState.EMPTY.ordinal();
    }

    @Override
    public String toString() {
        return board.toString().replace("0", "-").replace("1", "X").replace("2","O");
    }

    private class LinkedListOpenPositionsWrapper extends LinkedList<Integer> implements OpenPositions {
        @Override public int size(int playerNr) { return size(); }

        @Override public int get(int posIndex, int playerNr) { return get(posIndex); }

        @Override public int remove(int posIndex, int playerNr) { return remove(posIndex); }

        @Override public void add(int posIndex, int pos, int playerNr) {  add(posIndex, pos); }
    }

}
