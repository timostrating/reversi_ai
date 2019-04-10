package tic_tac_toe;

import game_util.MiniMaxHelper;
import game_util.Move;
import game_util.Player;
import util.OpenPosition;
import util.OpenPositions;

import java.util.LinkedList;

public class TicTacToeAIMiniMax extends Player {

    private TicTacToe game;

    MiniMaxHelper miniMaxHelper;

    public TicTacToeAIMiniMax(TicTacToe game) {
        this.game = game;
        miniMaxHelper = new MiniMaxHelper(game, game.board);
    }

    @Override
    public Move getInput() {
        MiniMaxHelper.PosAndScore best = miniMaxHelper.minimax(999, getNr(), game.openPositions);
        System.out.println(best);
        return best.move;
    }

}
