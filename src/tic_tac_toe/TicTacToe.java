package tic_tac_toe;

import Util.*;

import java.util.Arrays;

import static Util.BoardHelper.areAllEqual;

public class TicTacToe extends GameRules {

    public static final int BOARD_SIZE = 3;

    /* Enums are classes and should follow the conventions for classes. Instances of an enum are constants and should follow the conventions for constants.https://stackoverflow.com/a/3069863 */
    public enum CellState {EMPTY, X, O}
    public GameBoard2D board;

    @Override
    public void run() {
        this.board = new GameBoard2D(BOARD_SIZE);
        board.reset();
        int curPlayer = 0;
        while (board.containsCell(CellState.EMPTY.ordinal()) && this.getGameState() == GameState.PLAYING) {
            nextPlayer(players[curPlayer % 2]);
            curPlayer++;
        }
    }

    @Override
    public void playerPlays(Player p) {
        while (!playMove(p.getInput(), p.getNr()));
    }

    public GameState getGameState() {
        if(MatchOf3(CellState.X))
            return GameState.PLAYER_1_WINS;
        if(MatchOf3(CellState.O))
            return GameState.PLAYER_2_WINS;

        if (board.containsCell(CellState.EMPTY.ordinal()))
            return GameState.PLAYING;

        return GameState.DRAW;
    }

    private boolean MatchOf3(CellState player) {
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
        if (isValidMove(i) && getGameState() == GameState.PLAYING) {
            onValidMovePlayed.notifyObjects(o -> o.callback(i));
            board.set(i, CellState.values()[playerNr].ordinal());
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
