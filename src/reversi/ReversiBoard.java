package reversi;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Arrays;

public class ReversiBoard {
    private int[][] board;

    public ReversiBoard(int boardSize) {
        board = new int[boardSize][boardSize];
        board[boardSize/2 -1][boardSize/2 -1] = 1;
        board[boardSize/2 -1][boardSize/2   ] = 2;
        board[boardSize/2   ][boardSize/2   ] = 1;
        board[boardSize/2   ][boardSize/2 -1] = 2;
    }


    public static void main(String[] args) { // TESTING
        ReversiBoard board = new ReversiBoard(8);
        board.printBoard();
    }

    public void printBoard() { // TODO: move out
        for (int i=0; i<getBoardSize(); i++)
            System.out.println(Arrays.toString(board[i]));
    }


    public int[][] getBoard() { return board; }
    public int getBoardSize() { return board.length; }

    public boolean legalMove() {
        throw new NotImplementedException();
        // return moveOnBoard && ... TODO:
    }

    private boolean isInBounds(int x, int y) {
        return x < getBoardSize() && y < getBoardSize();
    }

    public void playMove(int x, int y, int playerNr) {
        throw new NotImplementedException();

//        board[y][x] = playerNr;
        // TODO: play rules
    }
}
