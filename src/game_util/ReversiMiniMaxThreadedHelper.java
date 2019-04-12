package game_util;

import reversi.Reversi;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static game_util.GameRules.GameState.*;

public class ReversiMiniMaxThreadedHelper extends MiniMaxHelper {

    final static int MAX_THREADS = 8;
    private static final ThreadPoolExecutor POOL = new ThreadPoolExecutor(MAX_THREADS, MAX_THREADS, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

    Reversi reversi;

    volatile int threadsInUse = 0;
    int startDepth;

    protected float eval(GameRules.GameState state, float[] playerScores) {

        boolean
                p1Wins = state == PLAYER_1_WINS,
                p2Wins = state == PLAYER_2_WINS,
                maxWins = (max == 1 && p1Wins) || (max == 2 && p2Wins),
                minWins = (min == 1 && p1Wins) || (min == 2 && p2Wins);
        if (maxWins)
            return +1000f;
        if (minWins)
            return -1000f;
        if (state == DRAW)
            return 100f;

        return playerScores[max] - playerScores[min];

    }

    public ReversiMiniMaxThreadedHelper(Reversi reversi, GameBoard2D board) {
        super(reversi, board, null);
        this.reversi = reversi;
    }

    @Override
    protected PosAndScore minimax(int depth, float alpha, float beta, int playerNr) {
        startDepth = depth;
        calls = 0;
        PosAndScore bla = minimax(
                depth, alpha, beta, playerNr,

                reversi
        );
        System.out.println("multithreaded calls: " + calls);
        return bla;
    }

    int calls = 0;

    protected PosAndScore minimax(
            int depth, float alpha, float beta, int playerNr,

            Reversi reversi
    ) {
        calls++;

        final PosAndScore best = new PosAndScore(-1, -1, null); // (position, score, move)
        if (playerNr == max)
            best.score = Integer.MIN_VALUE;
        else
            best.score = Integer.MAX_VALUE;

        AtomicInteger waitForThreads = new AtomicInteger(0);

        GameRules.GameState state = reversi.getGameSpecificState();

        if (depth == 0 || state != GameRules.GameState.PLAYING) { // TODO
            float score = eval(state, null);
            best.score = score;
            return best;
        }

        for (int posIndex = 0; posIndex < reversi.openPositions.size(playerNr); posIndex++) {

            final int pos = reversi.openPositions.get(posIndex, playerNr);

            Move m = reversi.getMove(pos, playerNr);
            m.doMove(false);

            if (threadsInUse < MAX_THREADS && startDepth == depth) {
                threadsInUse++;
                waitForThreads.incrementAndGet();
                // send work to a waiting thread.

                Reversi clonedReversi = new Reversi(reversi);
                float clonedAlpha = alpha, clonedBeta = beta;

                POOL.execute(() -> {

                    PosAndScore posAndScore = minimax(
                            depth-1, clonedAlpha, clonedBeta, (playerNr%2) +1,

                            clonedReversi
                    );

                    posAndScore.pos = pos;
                    posAndScore.move = m;

                    best.setIf(playerNr == max, posAndScore);

                    threadsInUse--;
                    waitForThreads.decrementAndGet();
                });
                m.undoMove();
                continue;
            }

            PosAndScore posAndScore = minimax(
                    depth-1, alpha, beta, (playerNr%2) +1,

                    reversi
            );

            m.undoMove();
            posAndScore.pos = pos;
            posAndScore.move = m;

            if (playerNr == max) {
                best.setIf(true, posAndScore);

                alpha = Math.max(alpha, posAndScore.score);
            }
            else {
                best.setIf(false, posAndScore);

                beta = Math.min(beta, posAndScore.score);
            }

            if (beta <= alpha)
                break;
        }

        while (waitForThreads.intValue() > 0) {}

        return best;
    }
}
