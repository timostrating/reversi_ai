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
        int randomInt = 0;

        while (!reversi.isValidMove(randomInt, getNr())) {
            randomInt = r.nextInt(Reversi.CELL_COUNT);
        }

        System.err.println(reversi.getOpenPositions(getNr()));


        return randomInt;


//        Reversi.OpenPositionsReversi openPositions = reversi.getOpenPositions(getNr());
//        return openPositions.get(r.nextInt(openPositions.size()));
    }
}
