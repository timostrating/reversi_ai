package game_util;

import util.OpenPosition;
import util.OpenPositions;

import static game_util.GameRules.GameState.*;

/**
 * We assume that you want to use the miniMax algorithm for a game with 2 players
 */
public class MiniMaxHelper<T extends OpenPosition> {

    GameRules game;
    GameBoard2D board;
    OpenPositions<T> openPositions;
    int max;
    int min;

    public MiniMaxHelper(GameRules game, GameBoard2D board) {
        this.game = game;
        this.board = board;
    }

    private float eval(GameRules.GameState state) {
        boolean
                p1Wins = state == PLAYER_1_WINS,
                p2Wins = state == PLAYER_2_WINS,
                maxWins = (max == 1 && p1Wins) || (max == 2 && p2Wins),
                minWins = (min == 1 && p1Wins) || (min == 2 && p2Wins);
        if (maxWins)
            return +100f / board.amount(max);
        if (minWins)
            return -100f / board.amount(min);
        if (state == DRAW)
            return 1f / board.amount(max);
        return 0;
    }

    public PosAndScore minimax(int depth, int player, OpenPositions<T> openPositions) {
        this.max = player;
        this.min = (player % 2) + 1;
        this.openPositions = openPositions;
        return minimax(depth, Integer.MIN_VALUE, Integer.MAX_VALUE, player);
    }
    private PosAndScore minimax(int depth, float alpha, float beta, int playerNr) {
        PosAndScore best = new PosAndScore(null, -1); // (position, score)

        if (playerNr == max)
            best.score = Integer.MIN_VALUE;
        else
            best.score = Integer.MAX_VALUE;

        if (depth == 0 || game.getGameSpecificState() != GameRules.GameState.PLAYING) { // TODO
            float score = eval(game.getGameSpecificState());
            best.score = score;
            return best;
        }

        for (int posIndex = 0; posIndex < openPositions.size(playerNr); posIndex++) {
            T pos = openPositions.get(posIndex, playerNr);
            board.set(pos.i, playerNr);
            openPositions.remove(posIndex, playerNr);


            PosAndScore posAndScore = minimax(depth-1, alpha, beta, (playerNr%2) +1);
            board.set(pos.i, 0);
            openPositions.add(posIndex, pos, playerNr);
            posAndScore.pos = pos;

            if (playerNr == max) {
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
        public OpenPosition pos;
        public float score;

        PosAndScore(OpenPosition pos, float score) {
            this.pos = pos;
            this.score = score;
        }

        @Override
        public String toString() {
            return "Position: " + pos + ", Score: " + score;
        }
    }
}
