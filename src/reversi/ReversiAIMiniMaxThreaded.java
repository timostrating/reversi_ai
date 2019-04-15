package reversi;

import game_util.*;

import java.util.concurrent.atomic.AtomicReference;

public class ReversiAIMiniMaxThreaded extends Player {

    private Reversi reversi;

    Reversi.OpenPositionsReversi openPositions;
    ReversiMiniMaxThreadedHelper
            miniMaxHelperDepth7, miniMaxHelperDepthDeeper;

    public ReversiAIMiniMaxThreaded(Reversi reversi) {
        this.reversi = reversi;
        miniMaxHelperDepth7 = new ReversiMiniMaxThreadedHelper(reversi, reversi.board, 0);
    }

    @Override
    public Move getInput() {

        AtomicReference<MiniMaxHelper.PosAndScore>
                bestDeeper = new AtomicReference<>(),
                best7 = new AtomicReference<>();


        try {

            Reversi cloned = new Reversi(reversi);

            new Thread(() -> {
                // depth 7:
                best7.set(miniMaxHelperDepth7.minimax(7, getNr(), reversi.getOpenPositions()));
                System.out.println("Best depth 7: " + best7);
            }).start();

            Thread deeperThread = new Thread(() -> {
                // DEEPER:
                miniMaxHelperDepthDeeper = new ReversiMiniMaxThreadedHelper(cloned, cloned.board, 6);
                bestDeeper.set(miniMaxHelperDepthDeeper.minimax(8, getNr(), cloned.getOpenPositions()));
                System.out.println("Best DEEPER: " + bestDeeper);
            });
            deeperThread.start();

            int time = 0;
            int step = 200;

            while (true) {
                try {
                    Thread.sleep(step);
                } catch (InterruptedException e) {
                }

                time += step;

                if (time >= 9500 || bestDeeper.get() != null) {

                    if (bestDeeper.get() != null) {
                        System.out.println("Choosing move of depth 8");
                        return reversi.getMove(bestDeeper.get().move.toI(), getNr());
                    } else {
                        System.out.println("Choosing move of depth 7");
                        deeperThread.stop();
                        return best7.get().move;
                    }

                }
            }

        } catch (Exception e) {

            System.err.println("AAAAAAAAAAa!!!!!!!");
            return miniMaxHelperDepth7.minimax(7, getNr(), reversi.getOpenPositions()).move;

        }
    }

}
