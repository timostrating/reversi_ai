package reversi;

import game_util.*;

import static game_util.GameRules.GameState.*;

public class ReversiAIMiniMaxThreaded extends Player {

    private Reversi reversi;

    Reversi.OpenPositionsReversi openPositions;
    ReversiMiniMaxThreadedHelper miniMaxHelper;

    public ReversiAIMiniMaxThreaded(Reversi reversi) {
        this.reversi = reversi;
        miniMaxHelper = new ReversiMiniMaxThreadedHelper(reversi, reversi.board);
    }

    @Override
    public Move getInput() {
        openPositions = reversi.getOpenPositions();
        MiniMaxHelper.PosAndScore best = miniMaxHelper.minimax(7, getNr(), openPositions);
        System.out.println(best);
        return best.move;
    }

}
