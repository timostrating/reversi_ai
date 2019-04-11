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
        return minimax(
                depth, alpha, beta, playerNr,

                reversi.playerScores, board, reversi.openPositions, false
        );
    }

    protected PosAndScore minimax(
            int depth, float alpha, float beta, int playerNr,

            // reversi specific things:
            float[] playerScores, GameBoard2D board, Reversi.OpenPositionsReversi openPositions,

            boolean isInSeperateThread
    ) {

        final PosAndScore best = new PosAndScore(-1, -1, null); // (position, score, move)
        if (playerNr == max)
            best.score = Integer.MIN_VALUE;
        else
            best.score = Integer.MAX_VALUE;

        AtomicInteger waitForThreads = new AtomicInteger(0);

        GameRules.GameState state = reversi.getGameSpecificState(board, openPositions);

        if (depth == 0 || state != GameRules.GameState.PLAYING) { // TODO
            float score = eval(state, playerScores);
            best.score = score;
            return best;
        }

        for (int posIndex = 0; posIndex < openPositions.size(playerNr); posIndex++) {

            int pos = openPositions.get(posIndex, playerNr);

            Move m = reversi.getMove(pos, playerNr, playerScores, board, openPositions);
            if (m == null) continue;
            m.doMove(false);

            if (threadsInUse < MAX_THREADS && startDepth == depth) {
                threadsInUse++;
                waitForThreads.incrementAndGet();
//                System.out.println("Starting minimax on seperate thread");
                // send work to a waiting thread.

                float[] clonedPlayerScores = playerScores.clone();
                GameBoard2D clonedBoard = board.clone();
                Reversi.OpenPositionsReversi clonedOpenPoss = openPositions.clone(clonedBoard);
                float clonedAlpha = alpha, clonedBeta = beta;

                POOL.execute(() -> {

                    PosAndScore posAndScore = minimax(
                            depth-1, clonedAlpha, clonedBeta, (playerNr%2) +1,

                            clonedPlayerScores, clonedBoard, clonedOpenPoss,

                            true
                    );

                    posAndScore.pos = pos;
                    posAndScore.move = m;

                    best.setIf(playerNr == max, posAndScore, startDepth == depth);

                    threadsInUse--;
                    waitForThreads.decrementAndGet();
//                    System.out.println("Minimax on seperate thread done.");

                });
                m.undoMove();
                continue;
            }

            PosAndScore posAndScore = minimax(
                    depth-1, alpha, beta, (playerNr%2) +1,

                    playerScores, board, openPositions,

                    false
            );

            m.undoMove();
            posAndScore.pos = pos;
            posAndScore.move = m;

            if (playerNr == max) {
                best.setIf(true, posAndScore, startDepth == depth);

                alpha = Math.max(alpha, posAndScore.score);
            }
            else {
                best.setIf(false, posAndScore, startDepth == depth);

                beta = Math.min(beta, posAndScore.score);
            }

            if (beta <= alpha)
                break;

        }

        while (waitForThreads.intValue() > 0) {
//            try {
//                Thread.sleep(10);
//            } catch (InterruptedException e) { }
        }
        if (startDepth == depth)
            for (int pos : openPositions.getOpenPositions(playerNr))
                System.out.println(board.iToX(pos) + ", " + board.iToY(pos));

        return best;
    }
}
