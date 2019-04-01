package tic_tac_toe;

import Util.HumanPlayer;

public class TicTacToeEngine {

    public TicTacToeEngine() {
        TicTacToe game = new TicTacToe();

        TicTacToeAIScore ai1 = new TicTacToeAIScore(1, game);
        HumanPlayer p2 = new HumanPlayer(2);

        game.onNextPlayer.register(() -> System.out.println(game.toString()));
        game.playGame(ai1, p2);
    }
}
