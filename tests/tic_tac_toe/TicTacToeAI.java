package tic_tac_toe;

import Util.Arcade;
import Util.Callback;
import Util.GameRules;
import org.junit.jupiter.api.Test;

import static Util.Arcade.*;

public class TicTacToeAI {

    TicTacToe game = new TicTacToe();
    Arcade arcade = new Arcade();
    Callback printBoard = () -> System.out.println(game.toString());

    @Test
    public void playAIvsAI() {
        game = (TicTacToe) arcade.createGame(GameFactory.TicTacToe, RefereeFactory.TicTacToeReferee,
                PlayerFactory.TicTacToeAIMiniMax, PlayerFactory.TicTacToeAIMiniMax);
        game.onNextPlayer.register(printBoard);
        game.onGameEnded.register(printBoard);
        game.run();

        assert game.getGameState() == GameRules.GameState.PLAYER_1_WINS;
        assert game.onNextPlayer.getTimesNotified() == 7;
    }

    @Test
    public void playAIvsAIScore() {
        game = (TicTacToe) arcade.createGame(GameFactory.TicTacToe, RefereeFactory.TicTacToeReferee,
                PlayerFactory.TicTacToeAIMiniMax, PlayerFactory.TicTacToeAIScore);
        game.onNextPlayer.register(printBoard);
        game.onGameEnded.register(printBoard);
        game.run();

        System.out.println(game.onNextPlayer.getTimesNotified());

        assert game.getGameState() == GameRules.GameState.PLAYER_1_WINS;
        assert game.onNextPlayer.getTimesNotified() == 7;
    }
}
