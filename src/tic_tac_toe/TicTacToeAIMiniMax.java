package tic_tac_toe;

import Util.GameRules;
import Util.Player;

import java.util.LinkedList;
import java.util.List;

import static Util.GameRules.GameState.*;

public class TicTacToeAIMiniMax extends Player {

    private class PosAndScore {
        int pos;
        float score;

        PosAndScore(int pos, float score) {
            this.pos = pos;
            this.score = score;
        }

        @Override
        public String toString() {
            return "Position: " + pos + ", Score: " + score;
        }
    }

    private TicTacToe game;
    private int setCount = 0;

    List<Integer> openPositions = new LinkedList<>();

    public TicTacToeAIMiniMax(TicTacToe game) {
        this.game = game;
        for (int i=0; i <= 8; i++)
            openPositions.add(i);
        game.onValidMovePlayed.register((x)->openPositions.remove(x));
        game.onNextPlayer.register(()->setCount++);
    }

    @Override
    public int getInput() {
        max = getNr();
        min = (getNr() % 2) + 1;
        PosAndScore best = minimax(999, Integer.MIN_VALUE, Integer.MAX_VALUE, max);
        System.out.println(best);
        return best.pos;
    }

    int max;
    int min;

    private float eval(GameRules.GameState state) {
        boolean
                p1Wins = state == PLAYER_1_WINS,
                p2Wins = state == PLAYER_2_WINS,
                maxWins = (max == 1 && p1Wins) || (max == 2 && p2Wins),
                minWins = (min == 1 && p1Wins) || (min == 2 && p2Wins);
        if (maxWins)
            return +1f / game.board.amount(max);
        if (minWins)
            return -1f / game.board.amount(min);
        if (state == DRAW)
            return 0.00001f / game.board.amount(max);
        return 0;
    }

    private PosAndScore minimax(int depth, float alpha, float beta, int player) {
        PosAndScore best = new PosAndScore(-1, -1); // (position, score)

        if (player == max)
            best.score = Integer.MIN_VALUE;
        else
            best.score = Integer.MAX_VALUE;

        if (depth == 0 || game.getGameState() != GameRules.GameState.PLAYING) { // TODO
            float score = eval(game.getGameState());
            best.score = score;
        }

        for (int posIndex = 0; posIndex < openPositions.size(); posIndex++) {
            int pos = openPositions.get(posIndex);
            game.board.set(pos, player);
            openPositions.remove(posIndex); // TODO


            PosAndScore posAndScore = minimax(depth-1, alpha, beta, (player%2) +1);
            game.board.set(pos, 0);
            openPositions.add(posIndex, pos);
            posAndScore.pos = pos;

            if (player == max) {
                if (best.score < posAndScore.score)
                    best = posAndScore;

                alpha = Math.max(alpha, posAndScore.score);
            }
            else {
                if (best.score > posAndScore.score)
                    best = posAndScore;

                beta = Math.min(beta, posAndScore.score);
            }

            if (beta <= alpha)
                break;

        }

        return best;
    }


}
