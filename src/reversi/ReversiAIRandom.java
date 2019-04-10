package reversi;

import game_util.Move;
import game_util.Player;
import util.OpenPosition;

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

        String str = "";
        for (int y = 0; y < Reversi.BOARD_SIZE; y++) {
            for (int x = 0; x < Reversi.BOARD_SIZE; x++) {
                str = (reversi.board.get(x, y) + " ").replace('0', '-');
                if (openPositions.getOpenPositions(getNr()).contains(new OpenPosition(reversi.board.xyToI(x, y))))
                    str = str.replace('-', 'v');

                System.out.print(str);
            }
            System.out.println("");
        }

        return reversi.getMove(openPositions.get(r.nextInt(openPositions.size(getNr())), getNr()).i, getNr());
    }
}
