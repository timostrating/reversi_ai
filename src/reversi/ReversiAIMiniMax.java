package reversi;

import game_util.MiniMaxHelper;
import game_util.Player;
import util.OpenPositions;

import java.util.LinkedList;

import static reversi.Reversi.BOARD_SIZE;

public class ReversiAIMiniMax extends Player {

    private Reversi reversi;

    LinkedListOpenPositionsWrapper openPositions = new LinkedListOpenPositionsWrapper();
    MiniMaxHelper miniMaxHelper;

    public ReversiAIMiniMax(Reversi reversi) {
        this.reversi = reversi;

        for (int i = 0; i <= BOARD_SIZE; i++)
            openPositions.add(i);
        reversi.onValidMovePlayed.register((x)->openPositions.remove(x));

        openPositions.remove(new Integer(4 * BOARD_SIZE + 4));
        openPositions.remove(new Integer(4 * BOARD_SIZE + 5));
        openPositions.remove(new Integer(5 * BOARD_SIZE + 4));
        openPositions.remove(new Integer(5 * BOARD_SIZE + 5));

        miniMaxHelper = new MiniMaxHelper(reversi, reversi.board, openPositions);
    }

    @Override
    public int getInput() {
        MiniMaxHelper.PosAndScore best = miniMaxHelper.minimax(999, getNr());
        System.out.println(best);
        return best.pos;
    }

    private class LinkedListOpenPositionsWrapper extends LinkedList<Integer> implements OpenPositions { }
}
