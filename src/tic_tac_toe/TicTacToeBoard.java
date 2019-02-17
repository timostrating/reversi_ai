package tic_tac_toe;

import Interfaces.Board;
import Util.BoardHelper;

public class TicTacToeBoard implements Board<TicTacToeBoard.CellState> {

    private static final int BOARD_SIZE = 3;

    public enum CellState {Empty, X, O}
    public enum GameState {Playing, Draw, Player1Wins, Player2Wins}
    private CellState[][] board;

    public TicTacToeBoard() {
        this.board = new CellState[BOARD_SIZE][BOARD_SIZE];
        this.reset();
    }

    public void reset() {
        for (int i=0; i<BOARD_SIZE; i++)
            for (int j=0; j<BOARD_SIZE; j++)
                board[i][j] = CellState.Empty;
    }

    public GameState getGameState() {
        if(isWinner(CellState.X))
            return GameState.Player1Wins;
        if(isWinner(CellState.O))
            return GameState.Player2Wins;

        if (movesLeft())
            return GameState.Playing;

        return GameState.Draw;
    }

    private boolean movesLeft() {
        for (int y=0; y<BOARD_SIZE; y++)
            for (int x=0; x<BOARD_SIZE; x++)
                if (board[y][x] == CellState.Empty)
                    return true;

        return false;
    }

    private boolean isWinner(CellState player) {
        return  BoardHelper.areAllEqual(player, board[0][0], board[0][1], board[0][2]) ||
                BoardHelper.areAllEqual(player, board[1][0], board[1][1], board[1][2]) ||
                BoardHelper.areAllEqual(player, board[2][0], board[2][1], board[2][2]) ||

                BoardHelper.areAllEqual(player, board[0][0], board[1][0], board[2][0]) ||
                BoardHelper.areAllEqual(player, board[0][1], board[1][1], board[2][1]) ||
                BoardHelper.areAllEqual(player, board[0][2], board[1][2], board[2][2]) ||

                BoardHelper.areAllEqual(player, board[0][0], board[1][1], board[2][2]) ||
                BoardHelper.areAllEqual(player, board[0][2], board[1][1], board[2][0]);
    }

    @Override
    public CellState[][] getBoard() {
        return board;
    }

    public boolean playMove(int x, int y, int playerNr) {
        if (isValidMove(x, y)) {
            board[y][x] = CellState.values()[playerNr];
            return true;
        }

        return false;
    }

    private boolean isValidMove(int x, int y) {
        return isInBounds(x, y) && board[y][x] == CellState.Empty;
    }

    private boolean isInBounds(int x, int y) {
        return x >= 0 && x < BOARD_SIZE && y >= 0 && y < BOARD_SIZE;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();


        sb.append("  0 1 2");
        for (int y = 0; y < BOARD_SIZE; y++) {
            sb.append("\n" +y+ " ");
            for (int x = 0; x < BOARD_SIZE; x++)
                sb.append((board[y][x] == CellState.Empty)? "- " : board[y][x].name()+" ");

        }

        return new String(sb);
    }

}
