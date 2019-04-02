package tic_tac_toe;

import Util.Callback;
import Util.HumanPlayer;
import Util.Player;

public class TicTacToeEngine {

    public TicTacToeEngine() {
        TicTacToe game = new TicTacToe();

        Player ai1 = new TicTacToeAIMiniMax(game);
        Player ai2 = new TicTacToeAIMiniMax(game);
        Player p2 = new HumanPlayer();

        Callback printBoard = () -> System.out.println(game.toString());

        game.onNextPlayer.register(printBoard);
        game.onGameEnded.register(printBoard);
    }
}
