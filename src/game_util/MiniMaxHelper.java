package game_util;

import util.OpenPositions;

/**
 * We assume that you want to use the miniMax algorithm for a game with 2 players
 */
public class MiniMaxHelper {

    GameRules game;
    GameBoard2D board;
    OpenPositions openPositions;
    Evaluator evaluator;
    int max;
    int min;

    public MiniMaxHelper(GameRules game, GameBoard2D board, Evaluator evaluator) {
        this.game = game;
        this.board = board;
        this.evaluator = evaluator;
    }

    @FunctionalInterface
    public interface Evaluator {
        float eval(GameRules.GameState state, int minPlayer, int maxPlayer);
    }

    private float eval(GameRules.GameState state) {
        return evaluator.eval(state, min, max);
    }

    public PosAndScore minimax(int depth, int player, OpenPositions openPositions) {
        this.max = player;
        this.min = (player % 2) + 1;
        this.openPositions = openPositions;
        return minimax(depth, Integer.MIN_VALUE, Integer.MAX_VALUE, player);
    }

    protected PosAndScore minimax(int depth, float alpha, float beta, int playerNr) {
        PosAndScore best = new PosAndScore(-1, -1, null); // (position, score, move)
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
            int pos = openPositions.get(posIndex, playerNr);

            Move m = game.getMove(pos, playerNr);
            if (m == null) continue;
            m.doMove(false);

            PosAndScore posAndScore = minimax(depth-1, alpha, beta, (playerNr%2) +1);

            m.undoMove();
            posAndScore.pos = pos;
            posAndScore.move = m;

            if (playerNr == max) {
                if (best.score < posAndScore.score || best.pos == -1)
                    best = posAndScore;

                alpha = Math.max(alpha, posAndScore.score);
            }
            else {
                if (best.score > posAndScore.score || best.pos == -1)
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
        public Move move;

        PosAndScore(int pos, float score, Move move) {
            this.pos = pos;
            this.score = score;
            this.move = move;
        }

        @Override
        public String toString() {
            return "Position: ("+board.iToX(pos)+", "+board.iToY(pos)+"), Score: " + score;
        }
    }
}
