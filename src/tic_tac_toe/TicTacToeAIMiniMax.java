package tic_tac_toe;

import Util.GameRules;
import Util.Player;

import java.util.LinkedList;
import java.util.List;

import static Util.GameRules.GameState.PLAYER_1_WINS;
import static Util.GameRules.GameState.PLAYER_2_WINS;

public class TicTacToeAIMiniMax implements Player {

    private int nr;
    private TicTacToe game;
    private int setCount = 0;

    List<Integer> openPositions = new LinkedList<>();

    public TicTacToeAIMiniMax(int nr, TicTacToe game) {
        this.nr = nr;
        this.game = game;
        for (int i=0; i <= 8; i++)
            openPositions.add(i);
        game.onValidMovePlayed.register((x)->openPositions.remove(x));
        game.onNextPlayer.register(()->setCount++);
    }

    @Override
    public int getNr() {
        return nr;
    }

    @Override
    public int getInput() {
        int[] best = minimax(999, Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
        System.out.println(best);
        return best[0];
    }

    final int MAX = 1;
    final int MIN = 2;

    private int eval(GameRules.GameState state) { // TODO
        if (state == PLAYER_1_WINS)
            return +1;
        if (state == PLAYER_2_WINS)
            return -1;
        return 0;
    }

    private int[] minimax(int depth, int alpha, int beta, int player) {
        int[] best = new int[]{-1, -1}; // (position, score)

        if (player == MAX)
            best[1] = Integer.MIN_VALUE;
        else
            best[1] = Integer.MAX_VALUE;

        if (depth == 0 || game.getGameState() != GameRules.GameState.PLAYING) { // TODO
            int score = eval(game.getGameState());
            best[1] = score;
        }

        for (int posIndex = 0; posIndex < openPositions.size(); posIndex++) {
            System.out.println(player);
            System.out.println(openPositions);
            System.out.println(game.board.toString());


            int pos = openPositions.get(posIndex);
            game.board.set(pos, player);
            openPositions.remove(posIndex); // TODO


            int[] posAndScore = minimax(depth-1, alpha, beta, (player%2) +1);
            game.board.set(pos, 0);
            openPositions.add(posIndex, pos);
            posAndScore[0] = pos;

            if (player == MAX) {
                if (best[1] < posAndScore[1])
                    best = posAndScore;

                alpha = Math.max(alpha, posAndScore[1]);
            }
            else {
                if (best[1] > posAndScore[1])
                    best = posAndScore;

                beta = Math.min(beta, posAndScore[1]);
            }

            if (beta <= alpha)
                break;

        }

        return best;
    }


}
