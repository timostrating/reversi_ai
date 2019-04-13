package tic_tac_toe;

import game_util.MiniMaxHelper;
import game_util.Move;
import game_util.Player;

import static game_util.GameRules.GameState.*;

public class TicTacToeAIMiniMax extends Player {

    private TicTacToe game;

    MiniMaxHelper miniMaxHelper;

    public TicTacToeAIMiniMax(TicTacToe game) {
        this.game = game;
        miniMaxHelper = new MiniMaxHelper(
                game, game.board,
                // evaluator:
                (state, min, max) -> {
                    boolean
                            p1Wins = state == PLAYER_1_WINS,
                            p2Wins = state == PLAYER_2_WINS,
                            maxWins = (max == 1 && p1Wins) || (max == 2 && p2Wins),
                            minWins = (min == 1 && p1Wins) || (min == 2 && p2Wins);
                    if (maxWins)
                        return +100f / game.board.amount(max);
                    if (minWins)
                        return -100f / game.board.amount(min);
                    if (state == DRAW)
                        return 1f / game.board.amount(max);
                    return 0;
                }
        );
    }

    @Override
    public Move getInput() {
        MiniMaxHelper.PosAndScore best = miniMaxHelper.minimax(999, getNr(), game.openPositions);
        System.out.println(game.openPositions);
        System.out.println(best);
        return best.move;
    }

}
