package reversi;

import game_util.Move;
import game_util.Player;

import java.util.Random;

public class ReversiAIRandom extends Player {

    private Reversi reversi;
    Random r = new Random();

    public ReversiAIRandom(Reversi reversi) {
        this.reversi = reversi;
    }

    @Override
    protected Move getInput() {
        Reversi.OpenPositionsReversi openPositions = reversi.getOpenPositions();
        System.out.println(openPositions);

        System.out.println();
        System.out.println();
        String str = "";
        for (int y = 0; y < Reversi.BOARD_SIZE; y++) {
            for (int x = 0; x < Reversi.BOARD_SIZE; x++) {
                str = (reversi.board.get(x, y) + " ").replace('0', '-');
                boolean openForMe = openPositions.getOpenPositions(getNr()).contains(reversi.board.xyToI(x, y));
                boolean openForOther = openPositions.getOpenPositions((getNr() % 2) + 1).contains(reversi.board.xyToI(x, y));
                if (openForMe && openForOther)
                    str = str.replace('-', '⊕');
                else if (openForMe)
                    str = str.replace('-', getNr() == 1 ? 'x' : 'o');
                else if (openForOther)
                    str = str.replace('-', getNr() == 2 ? 'x' : 'o');

                System.out.print(str);
            }
            System.out.println();
        }
        System.out.println();
        System.out.println();

        return reversi.getMove(openPositions.get(r.nextInt(openPositions.size(getNr())), getNr()), getNr());
    }
}
