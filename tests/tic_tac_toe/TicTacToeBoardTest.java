package tic_tac_toe;

import Util.Arcade;
import Util.GameRules;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TicTacToeBoardTest {

    TicTacToe board;
    Arcade arcade = new Arcade();

    @BeforeEach
    void BeforeEach() {
        board = (TicTacToe) arcade.createGame(Arcade.GameFactory.TicTacToe, Arcade.RefereeFactory.TicTacToeReferee,
                Arcade.PlayerFactory.HumanPlayer, Arcade.PlayerFactory.HumanPlayer);
    }

    @Test
    void playMove() {
        board.playMove(0, 1);
        board.playMove(2, 2);
        board.playMove(5, 1);
        board.playMove(4, 2);
        board.playMove(8, 1);
        board.playMove(6, 2);
        assert board.getGameState() == GameRules.GameState.PLAYER_2_WINS;
    }

    @Test
    void playMove2() {
        board.playMove(0, 1);
        board.playMove(1, 2);
        board.playMove(2, 1);
        board.playMove(7, 2);
        board.playMove(8, 1);
        board.playMove(3, 2);
        board.playMove(5, 1);
        assert board.getGameState() == GameRules.GameState.PLAYER_1_WINS;
        assert board.playMove(6, 2) == false;
    }

    @Test
    void invalidMove() {
        board.playMove(0, 1);
        board.playMove(1, 2);
        assert board.playMove(0, 1) == false;
    }
}