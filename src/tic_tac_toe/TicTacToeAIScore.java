package tic_tac_toe;

import game_util.Player;

// Renamed van TicTacToeAI naar TicTacToeAIScore
public class TicTacToeAIScore extends Player {

    private final TicTacToe game;

    int[] score = new int[]{
        3, 2, 3,
        2, 4, 2,
        3, 2, 3
    };

    public TicTacToeAIScore(TicTacToe game) {
        this.game = game;
    }

    @Override
    public int getInput() {
        int[] board = game.board.getBoard();

        int highScore = 0;
        int highScoreIndex = 0;
        for (int i=0; i<score.length; i++) {
            if (board[i] == 0) {
                if (highScore < score[i]) {
                    highScore = score[i];
                    highScoreIndex = i;
                }
            }
        }

        return highScoreIndex;
    }
}
