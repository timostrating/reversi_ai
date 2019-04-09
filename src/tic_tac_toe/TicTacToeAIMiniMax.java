package tic_tac_toe;

import game_util.MiniMaxHelper;
import game_util.Player;
import util.OpenPositions;

import java.util.LinkedList;

public class TicTacToeAIMiniMax extends Player {

    private TicTacToe game;

    LinkedListOpenPositionsWrapper openPositions = new LinkedListOpenPositionsWrapper();
    MiniMaxHelper miniMaxHelper;

    public TicTacToeAIMiniMax(TicTacToe game) {
        this.game = game;
        for (int i=0; i <= 8; i++)
            openPositions.add(i);
        game.onValidMovePlayed.register((x)->openPositions.remove(x.getKey()));

        miniMaxHelper = new MiniMaxHelper(game, game.board);
    }

    @Override
    public int getInput() {
        MiniMaxHelper.PosAndScore best = miniMaxHelper.minimax(999, getNr(), openPositions);
        System.out.println(best);
        return best.pos;
    }

    private class LinkedListOpenPositionsWrapper extends LinkedList<Integer> implements OpenPositions { }

}
