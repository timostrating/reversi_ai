package tic_tac_toe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TicTacToeBoardTest {

    TicTacToeBoard board;

    @BeforeEach
    void BeforeEach() {
        board = new TicTacToeBoard();
    }

    @Test
    void playMove() {
        board.playMove(0, 0, 1);
        board.playMove(0, 2, 2);
        board.playMove(1, 2, 1);
        board.playMove(1, 1, 2);
        board.playMove(2, 2, 1);
        board.playMove(2, 0, 2);
        assert board.getGameState() == TicTacToeBoard.GameState.PLAYER_2_WINS;
    }
}