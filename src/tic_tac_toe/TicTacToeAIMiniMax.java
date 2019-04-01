package tic_tac_toe;

import Util.GameRules;
import Util.Player;

import java.util.LinkedList;
import java.util.List;

import static Util.GameRules.GameState.PLAYER_1_WINS;
import static Util.GameRules.GameState.PLAYER_2_WINS;

public class TicTacToeAIMiniMax implements Player {

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

    private int nr;
    private TicTacToe game;
    private int setCount = 0;

    List<Integer> openPositions = new LinkedList<>();

    public TicTacToeAIMiniMax(int nr, TicTacToe game) {
        this.nr = nr;
        this.game = game;
        for (int i=0; i <= 8; i++)
            openPositions.add(i);
        game.onValidMovePlayed.register((x)->openPositions.remove(x));
        game.onNextPlayer.register(()->setCount++);
    }

    @Override
    public int getNr() {
        return nr;
    }

    @Override
    public int getInput() {
        PosAndScore best = minimax(999, Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
        System.out.println(best);
        return best.pos;
    }

    final int MAX = 1;
    final int MIN = 2;

    private float eval(GameRules.GameState state) { // TODO
        if (state == PLAYER_1_WINS)
            return +1f / game.board.amount(MAX);
        if (state == PLAYER_2_WINS)
            return -1f / game.board.amount(MIN);
        return 0;
    }

    private PosAndScore minimax(int depth, float alpha, float beta, int player) {
        PosAndScore best = new PosAndScore(-1, -1); // (position, score)

        if (player == MAX)
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

            if (player == MAX) {
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
