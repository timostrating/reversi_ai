package reversi;

import game_util.MiniMaxHelper;
import game_util.Move;
import game_util.Player;
import util.OpenPosition;

public class ReversiAIMiniMax extends Player {

    private Reversi reversi;

    Reversi.OpenPositionsReversi openPositions;
    MiniMaxHelper<OpenPosition> miniMaxHelper;

    public ReversiAIMiniMax(Reversi reversi) {
        this.reversi = reversi;
        miniMaxHelper = new MiniMaxHelper<>(reversi, reversi.board);
    }

    @Override
    public Move getInput() {
        openPositions = reversi.getOpenPositions();
        MiniMaxHelper.PosAndScore best = miniMaxHelper.minimax(4, getNr(), openPositions);
        System.out.println(best);
        return best.move;
    }

}
