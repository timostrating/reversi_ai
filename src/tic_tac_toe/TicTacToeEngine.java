package tic_tac_toe;

import Util.HumanPlayer;
import Util.Player;

public class TicTacToeEngine {

    public static void main(String[] args) {
        TicTacToe game = new TicTacToe();

        Player ai1 = new TicTacToeAIMiniMax(1, game);
        Player p2 = new HumanPlayer(2);

        game.onNextPlayer.register(() -> System.out.println(game.toString()));
        game.playGame(ai1, p2);
    }
}
