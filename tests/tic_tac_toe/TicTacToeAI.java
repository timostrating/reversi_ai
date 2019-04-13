package tic_tac_toe;

import game_util.Arcade;
import util.Callback;
import game_util.GameRules;
import org.junit.jupiter.api.Test;

import static game_util.Arcade.*;

public class TicTacToeAI {

    TicTacToe game = new TicTacToe();
    Arcade arcade = new Arcade();
    Callback printBoard = () -> System.out.println(game.toString());

    @Test
    public void playAIvsAI() {
        game = (TicTacToe) arcade.createGame(GameFactory.TicTacToe, RefereeFactory.DefaultReferee,
                PlayerFactory.TicTacToeAIMiniMax, PlayerFactory.TicTacToeAIMiniMax);
        game.onNextPlayer.register(printBoard);
        game.onGameEnded.register(printBoard);
        game.run();

        System.out.print(game.getGameState() );
        assert game.getGameState() == GameRules.GameState.DRAW;
        assert game.onNextPlayer.getTimesNotified() == 9;
    }

    @Test
    public void playAIvsAIScore() {
        game = (TicTacToe) arcade.createGame(GameFactory.TicTacToe, RefereeFactory.DefaultReferee,
                PlayerFactory.TicTacToeAIMiniMax, PlayerFactory.TicTacToeAIScore);
        game.onNextPlayer.register(printBoard);
        game.onGameEnded.register(printBoard);
        game.run();

        System.out.println(game.onNextPlayer.getTimesNotified());

        assert game.getGameState() == GameRules.GameState.PLAYER_1_WINS;
        assert game.onNextPlayer.getTimesNotified() == 7;
    }
}
