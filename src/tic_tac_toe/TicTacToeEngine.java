package tic_tac_toe;

import Util.HumanPlayer;

public class TicTacToeEngine {

    public static void main(String[] args) {
        TicTacToe game = new TicTacToe();

        TicTacToeAI ai1 = new TicTacToeAI(1, game);
        HumanPlayer p2 = new HumanPlayer(2);

        game.onNextPlayer.register(() -> System.out.println(game.toString()));
        game.playGame(ai1, p2);
    }
}
