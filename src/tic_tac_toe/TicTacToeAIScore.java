package tic_tac_toe;

import Util.Player;

// Renamed van TicTacToeAI naar TicTacToeAIScore
public class TicTacToeAIScore implements Player {

    private final int nr;
    private final TicTacToe game;

    int[] score = new int[]{
        3, 2, 3,
        2, 4, 2,
        3, 2, 3
    };

    public TicTacToeAIScore(int nr, TicTacToe game) {
        this.nr = nr;
        this.game = game;
    }

    @Override
    public int getNr() {
        return nr;
    }

    @Override
    public int getInput() {
        int[][] board = game.board.getBoard();

        int highScore = 0;
        int highScoreIndex = 0;
        for (int i=0; i<score.length; i++) {
            if (board[i/3][i%3] == 0) {
                if (highScore < score[i]) {
                    highScore = score[i];
                    highScoreIndex = i;
                }
            }
        }

        return highScoreIndex;
    }
}
