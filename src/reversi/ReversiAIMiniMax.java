package reversi;

import game_util.GameRules;
import game_util.MiniMaxHelper;
import game_util.Move;
import game_util.Player;

import java.util.LinkedList;

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

            return reversi.playerScores[max] - reversi.playerScores[min];
        }
    }

    public ReversiAIMiniMax(Reversi reversi) {
        this.reversi = reversi;
        miniMaxHelper = new MiniMaxHelper(reversi, reversi.board, new ReversiEvaluator());
    }

    @Override
    public Move getInput() {
        openPositions = reversi.getOpenPositions();

        // Killer move detection
        LinkedList<Integer> myOpenPositions = openPositions.getOpenPositions(getNr());
        if (myOpenPositions.get(0) == 0)
            return reversi.getMove(1, getNr());
        if (myOpenPositions.get(myOpenPositions.size() -1) == reversi.board.xyToI(7, 7))
            return reversi.getMove(reversi.board.xyToI(7, 7), getNr());
        if (myOpenPositions.contains(reversi.board.xyToI(0, 7)))
            return reversi.getMove(reversi.board.xyToI(0, 7), getNr());
        if (myOpenPositions.contains(reversi.board.xyToI(7, 0)))
            return reversi.getMove(reversi.board.xyToI(7, 0), getNr());

        MiniMaxHelper.PosAndScore best = miniMaxHelper.minimax(5, getNr(), openPositions);
        System.out.println("__Player_"+getNr() +"__ "+ best.toString());
        return best.move;
    }

}
