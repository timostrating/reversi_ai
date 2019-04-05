package reversi;

import game_util.Player;

import java.util.Random;

public class ReversiAIRandom extends Player {

    private Reversi reversi;
    Random r = new Random();

    public ReversiAIRandom(Reversi reversi) {
        this.reversi = reversi;
    }

    @Override
    protected int getInput() {
        if (getNr() == 2) {
            int randomInt = 0;
            while (!reversi.isValidMove(randomInt, getNr()))
                randomInt = r.nextInt(Reversi.CELL_COUNT);

            return randomInt;
        }

        Reversi.OpenPositionsReversi openPositions = reversi.getOpenPositions(getNr());
        System.out.println(openPositions);
        return openPositions.get(r.nextInt(openPositions.size()));
    }
}
