package tic_tac_toe;

import game_util.MiniMaxHelper;
import game_util.Player;
import util.OpenPositions;

import java.util.LinkedList;
import java.util.List;

public class TicTacToeAIMiniMax extends Player {

    private TicTacToe game;

    List<Integer> openPositions = new LinkedList<>();
    MiniMaxHelper miniMaxHelper;

    public TicTacToeAIMiniMax(TicTacToe game) {
        this.game = game;
        for (int i=0; i <= 8; i++)
            openPositions.add(i);
        game.onValidMovePlayed.register((x)->openPositions.remove(x));

        miniMaxHelper = new MiniMaxHelper(game, game.board, openPositions);
    }

    @Override
    public int getInput() {
        MiniMaxHelper.PosAndScore best = miniMaxHelper.minimax(999, getNr());
        System.out.println(best);
        return best.pos;
    }

    class openPositionsList extends LinkedList implements OpenPositions {
        List<Integer> openPositions = new LinkedList<>();
    }

}
