package reversi;

import game_util.GameRules;
import game_util.MiniMaxHelper;
import game_util.Move;
import game_util.Player;

import static game_util.GameRules.GameState.*;

public class ReversiAIMiniMax extends Player {

    private Reversi reversi;

    Reversi.OpenPositionsReversi openPositions;
    MiniMaxHelper miniMaxHelper;

    class ReversiEvaluator implements MiniMaxHelper.Evaluator {

        @Override
        public float eval(GameRules.GameState state, int min, int max) {
            boolean
                    p1Wins = state == PLAYER_1_WINS,
                    p2Wins = state == PLAYER_2_WINS,
                    maxWins = (max == 1 && p1Wins) || (max == 2 && p2Wins),
                    minWins = (min == 1 && p1Wins) || (min == 2 && p2Wins);
            if (maxWins)
                return +1000f;
            if (minWins)
                return -1000f;
            if (state == DRAW)
                return 100f;

            return reversi.playerScores[getNr() - 1][max] - reversi.playerScores[getNr() - 1][min];
        }
    }

    public ReversiAIMiniMax(Reversi reversi) {
        this.reversi = reversi;
        miniMaxHelper = new MiniMaxHelper(reversi, reversi.board, new ReversiEvaluator());
    }

    @Override
    public Move getInput() {
        openPositions = reversi.getOpenPositions();
        MiniMaxHelper.PosAndScore best = miniMaxHelper.minimax(7, getNr(), openPositions);
//        System.out.println("__Player_"+getNr() +"__ "+ best.toString());
        return best.move;
    }

}
