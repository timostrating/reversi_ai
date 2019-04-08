package tic_tac_toe;

import game_util.MiniMaxHelper;
import game_util.Player;
import util.OpenPosition;
import util.OpenPositions;

import java.util.LinkedList;

public class TicTacToeAIMiniMax extends Player {

    private TicTacToe game;

    LinkedListOpenPositionsWrapper openPositions = new LinkedListOpenPositionsWrapper();
    MiniMaxHelper miniMaxHelper;

    public TicTacToeAIMiniMax(TicTacToe game) {
        this.game = game;
        for (int i=0; i <= 8; i++)
            openPositions.add(new OpenPosition(i));
        game.onValidMovePlayed.register((x)->openPositions.filter(x, 0));

        miniMaxHelper = new MiniMaxHelper(game, game.board);
    }

    @Override
    public int getInput() {
        MiniMaxHelper.PosAndScore best = miniMaxHelper.minimax(999, getNr(), openPositions);
        System.out.println(best);
        return best.pos.i;
    }

    private class LinkedListOpenPositionsWrapper extends LinkedList<OpenPosition> implements OpenPositions {
        @Override public int size(int playerNr) { return size(); }

        @Override public OpenPosition get(int posIndex, int playerNr) { return get(posIndex); }

        @Override public OpenPosition remove(int posIndex, int playerNr) { return remove(posIndex); }

        @Override public void add(int posIndex, OpenPosition pos, int playerNr) {  add(posIndex, pos); }

    }

}
