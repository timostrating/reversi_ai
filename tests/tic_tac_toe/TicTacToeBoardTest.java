package tic_tac_toe;

import Util.GameRules;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TicTacToeBoardTest {

    TicTacToe board;

    @BeforeEach
    void BeforeEach() {
        board = new TicTacToe();
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
}