package tic_tac_toe;

import Util.Player;

import java.util.ArrayList;

public class TicTacToeAIMiniMax implements Player {

    private int nr;
    private TicTacToe game;

    ArrayList<Integer> openPositions = new ArrayList<>();

    public TicTacToeAIMiniMax(int nr, TicTacToe game) {
        this.nr = nr;
        this.game = game;
        for (int i=1; i <= 9; i++)
            openPositions.add(i);
        game.onValidMovePlayed.register((x)-> openPositions.remove(x));
    }

    @Override
    public int getNr() {
        return nr;
    }

    @Override
    public int getInput() {
        System.out.println(minimax());
        return 0;
    }


    private int minimax() {
//        int[] best = new int[]{-1, -1};
        for (int pos : openPositions) {
            System.out.println(pos);
        }

        return -1;
    }


}
