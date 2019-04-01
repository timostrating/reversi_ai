package tic_tac_toe;

import Util.Callback;
import Util.GameRules;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TicTacToeAI {

    TicTacToe game = new TicTacToe();
    Callback printBoard = () -> System.out.println(game.toString());

    @BeforeEach
    void BeforeEach() {
        game = new TicTacToe();
        game.onNextPlayer.register(printBoard);
        game.onGameOver.register(printBoard);
    }

    @Test
    public void playAIvsAI() {
        game.playGame(new TicTacToeAIMiniMax(game), new TicTacToeAIMiniMax(game));

        assert game.getGameState() == GameRules.GameState.PLAYER_1_WINS;
        assert game.onNextPlayer.getTimesNotified() == 7;
    }

    @Test
    public void playAIvsAIScore() {
        game.playGame(new TicTacToeAIMiniMax(game), new TicTacToeAIScore(game));

        assert game.getGameState() == GameRules.GameState.PLAYER_1_WINS;
        assert game.onNextPlayer.getTimesNotified() == 7;
    }
}
