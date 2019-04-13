package kermit_learning;

import game_util.DefaultReferee;
import reversi.Reversi;
import reversi.ReversiAIMiniMaxOptimized;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class LearnScores {

    private static final ThreadPoolExecutor POOL = new ThreadPoolExecutor(8, 8, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

    int generationNr = 0;

    static class Bot {
        double[][] usedScores;
        int result;

        public Bot(double[][] usedScores, int result) {
            this.usedScores = usedScores;
            this.result = result;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        LearnScores learnScores = new LearnScores();

        Bot[] currentGeneration = {
                new Bot(Reversi.DEFAULT_SCORES, 0),
                new Bot(Reversi.DEFAULT_SCORES, 0),
                new Bot(Reversi.DEFAULT_SCORES, 0),
                new Bot(Reversi.DEFAULT_SCORES, 0)
        };

        while (true) {
            currentGeneration = learnScores.nextGeneration(currentGeneration);
            learnScores.generationNr++;
        }
    }

    Bot[] nextGeneration(Bot[] oldGeneration) throws InterruptedException {
        AtomicInteger[] results = new AtomicInteger[4];
        Bot[] entries = new Bot[8];

        for (int i = 0; i < 4; i++) {

            double[][] scores0 = oldGeneration[i].usedScores;
            double[][] scores1 = mutateScores(oldGeneration[i].usedScores);

            if (generationNr % 10 == 0) {
                scores1 = oldGeneration[((i + 1) % 4)].usedScores;
            }

            AtomicInteger result = new AtomicInteger();
            results[i] = result;

            entries[i * 2] = new Bot(scores0, 0);
            entries[i * 2 + 1] = new Bot(scores1, 0);
            for (int j = 0; j < 2; j++) {


                Reversi reversi = j == 0 ? new Reversi(scores0, scores1) : new Reversi(scores1, scores0);

                reversi.initialize(new DefaultReferee(reversi), new ReversiAIMiniMaxOptimized(reversi), new ReversiAIMiniMaxOptimized(reversi));
                int finalJ = j;
                reversi.onGameEnded.register(() -> {
                    System.err.println("game done");
                    if (finalJ == 0)
                        result.addAndGet(reversi.board.amount(1) - reversi.board.amount(2));
                    else
                        result.addAndGet(reversi.board.amount(2) - reversi.board.amount(1));

//                    result.addAndGet(((finalJ == 0)? 1 : -1 ) * reversi.board.boardMlScore());
                });
                POOL.execute(reversi);
            }

        }
        while (POOL.getActiveCount() > 0) {
            Thread.sleep(50);
        }


        Bot[] nextGeneration = new Bot[4];

        for (int i = 0; i < 4; i++) {
            int result = results[i].get();

            Bot won = result >= 0 ? entries[i * 2] : entries[i * 2 + 1];
            won.result = Math.abs(result);
            nextGeneration[i] = won;

            System.out.println("\nBot won with result: " + result);
            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 8; y++) {
                    System.out.printf("%04f \t", won.usedScores[x][y]);
                }
                System.out.println();
            }
        }
        System.out.println("===========================");
        System.out.println(" Generation: " + generationNr);
        System.out.println("===========================");
        return entries;
    }

    double[][] mutateScores(double[][] old) {

        double[] a = new double[] {
                old[0][0], old[1][0], old[2][0], old[3][0],

                           old[1][1], old[2][1], old[3][1],

                                      old[2][2], old[3][2],

                                                 old[3][3]
        };

        for (int i = 0; i < 4; i++) {
            int j = (int) (Math.random() * a.length);
            // gradient descent:
            a[j] += (Math.random() - .5) * Math.min(.01f, (100f - generationNr) / 100f);
        }

        double[][] _new = new double[][] {
                {a[0], a[1], a[2], a[3], a[3], a[2], a[1], a[0]},

                {a[1], a[4], a[5], a[6], a[6], a[5], a[4], a[1]},

                {a[2], a[5], a[7], a[8], a[8], a[7], a[5], a[2]},

                {a[3], a[6], a[8], a[9], a[9], a[8], a[6], a[3]},

                {a[3], a[6], a[8], a[9], a[9], a[8], a[6], a[3]},

                {a[2], a[5], a[7], a[8], a[8], a[7], a[5], a[2]},

                {a[1], a[4], a[5], a[6], a[6], a[5], a[4], a[1]},

                {a[0], a[1], a[2], a[3], a[3], a[2], a[1], a[0]}
        };
        return _new;
    }

}
