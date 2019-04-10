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

    public PosAndScore minimax(int depth, int player, OpenPositions<T> openPositions) {
        this.max = player;
        this.min = (player % 2) + 1;
        this.openPositions = openPositions;
        return minimax(depth, Integer.MIN_VALUE, Integer.MAX_VALUE, player);
    }

    private PosAndScore minimax(int depth, float alpha, float beta, int playerNr) {
        PosAndScore best = new PosAndScore(null, -1, null); // (position, score, move)
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

//            System.out.println(openPositions);
//            String str = "";
//            for (int y = 0; y < Reversi.BOARD_SIZE; y++) {
//                for (int x = 0; x < Reversi.BOARD_SIZE; x++) {
//                    str = (board.get(x, y) + " ").replace('0', '-');
//                    if (openPositions.contains(board.xyToI(x, y), playerNr))
//                        str = str.replace('-', 'v');
//
//                    System.out.print(str);
//                }
//                System.out.println("");
//            }
//            System.out.println();
//            System.out.println();
            Move m = game.getMove(pos.i, playerNr);
            if (m == null)
                continue;
            m.doMove(false);


            PosAndScore posAndScore = minimax(depth-1, alpha, beta, (playerNr%2) +1);

            m.undoMove();
            posAndScore.pos = pos;
            posAndScore.move = m;

            if (playerNr == max) {
                if (best.score < posAndScore.score || best.pos == null)
                    best = posAndScore;

                alpha = Math.max(alpha, posAndScore.score);
            }
            else {
                if (best.score > posAndScore.score || best.pos == null)
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
        public Move move;

        PosAndScore(OpenPosition pos, float score, Move move) {
            this.pos = pos;
            this.score = score;
            this.move = move;
        }

        @Override
        public String toString() {
            if (pos == null)
                return "No position, score : " + score;
            return "Position: ("+board.iToX(pos.i)+", "+board.iToY(pos.i)+"), Score: " + score;
        }
    }
}
