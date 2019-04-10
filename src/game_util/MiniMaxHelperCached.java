package game_util;

public class MiniMaxHelperCached extends MiniMaxHelper {
    public MiniMaxHelperCached(GameRules game, GameBoard2D board, Evaluator evaluator) {
        super(game, board, evaluator);
    }

    @Override
    protected PosAndScore minimax(int depth, float alpha, float beta, int playerNr) {
        // TODO there are rotations and flips that are the same
        return super.minimax(depth,alpha, beta, playerNr);
    }
}