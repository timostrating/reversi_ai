package reversi;

import game_util.MiniMaxHelper;
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
    public int getInput() {
        openPositions = reversi.getOpenPositions();
        MiniMaxHelper.PosAndScore best = miniMaxHelper.minimax(999, getNr(), openPositions);
        System.out.println(best);
        return best.pos.i;
    }

}
