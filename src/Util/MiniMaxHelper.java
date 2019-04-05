package Util;

import java.util.List;

import static Util.GameRules.GameState.DRAW;
import static Util.GameRules.GameState.PLAYER_1_WINS;
import static Util.GameRules.GameState.PLAYER_2_WINS;

/**
 * We assume that you want to use the miniMax algorithm for a game with 2 players
 */
public class MiniMaxHelper {

    GameRules game;
    GameBoard2D board;
    List<Integer> openPositions;
    int max;
    int min;

    public MiniMaxHelper(GameRules game, GameBoard2D board, List<Integer> openPositions) {
        this.game = game;
        this.board = board;
        this.openPositions = openPositions;
    }

    private float eval(GameRules.GameState state) {
        boolean
                p1Wins = state == PLAYER_1_WINS,
                p2Wins = state == PLAYER_2_WINS,
                maxWins = (max == 1 && p1Wins) || (max == 2 && p2Wins),
                minWins = (min == 1 && p1Wins) || (min == 2 && p2Wins);
        if (maxWins)
            return +1f / board.amount(max);
        if (minWins)
            return -1f / board.amount(min);
        if (state == DRAW)
            return 0.00001f / board.amount(max);
        return 0;
    }

    public PosAndScore minimax(int depth, int player) {
        this.max = player;
        this.min = (player % 2) + 1;
        return minimax(depth, Integer.MIN_VALUE, Integer.MAX_VALUE, player);
    }
    private PosAndScore minimax(int depth, float alpha, float beta, int player) {
        PosAndScore best = new PosAndScore(-1, -1); // (position, score)

        if (player == max)
            best.score = Integer.MIN_VALUE;
        else
            best.score = Integer.MAX_VALUE;

        if (depth == 0 || game.getGameSpecificState() != GameRules.GameState.PLAYING) { // TODO
            float score = eval(game.getGameSpecificState());
            best.score = score;
        }

        for (int posIndex = 0; posIndex < openPositions.size(); posIndex++) {
            int pos = openPositions.get(posIndex);
            board.set(pos, player);
            openPositions.remove(posIndex);


            PosAndScore posAndScore = minimax(depth-1, alpha, beta, (player%2) +1);
            board.set(pos, 0);
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

    public class PosAndScore {
        public int pos;
        public float score;

        PosAndScore(int pos, float score) {
            this.pos = pos;
            this.score = score;
        }

        @Override
        public String toString() {
            return "Position: " + pos + ", Score: " + score;
        }
    }
}
