package game_util;

public class MiniMaxHelperCached extends MiniMaxHelper {
    public MiniMaxHelperCached(GameRules game, GameBoard board, Evaluator evaluator) {
        super(game, board, evaluator);
    }

    @Override
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

            if (m.toI() == board.xyToI(0, 0) || m.toI() == board.xyToI(7, 0) ||
                m.toI() == board.xyToI(0, 7) || m.toI() == board.xyToI(7, 7))
                return posAndScore;

            if (beta <= alpha)
                break;

        }

        return best;
    }
}