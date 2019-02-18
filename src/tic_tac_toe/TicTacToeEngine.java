package tic_tac_toe;

import Util.HumanPlayer;

public class TicTacToeEngine {

    public static void main(String[] args) {

        HumanPlayer p1 = new HumanPlayer(1);
        HumanPlayer p2 = new HumanPlayer(2);

        TicTacToeBoard board = new TicTacToeBoard();
        board.onNextPlayer.add(() -> System.out.println(board.toString()));
        board.playGame(p1, p2);
    }
}
