package reversi;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

class ReversiBoardTest {

    Reversi board = new Reversi();

    @BeforeEach
    void BeforeEach() {
        board = new Reversi();
    }

    @AfterEach
    void AfterEach() {
        System.out.println(board);
        board.reset();
    }

    @Test void playExampleMoves1a() {
        assert board.playMove(4, 2, 1);
        assert board.playMove(5, 2, 2);
        assert board.playMove(6, 2, 1);
        assert board.playMove(6, 1, 2);
        assert board.playMove(6, 0, 1);
        assert board.playMove(7, 0, 2);
        assert board.playMove(5, 3, 1);
        assert board.playMove(5, 4, 2);
    }
    @Test void playExampleMoves1b() {
        playExampleMoves1a();

        Reversi board2 = new Reversi();
        assert board2.playMove(4 + (2 * board2.BOARD_SIZE), 1);
        assert board2.playMove(5 + (2 * board2.BOARD_SIZE), 2);
        assert board2.playMove(6 + (2 * board2.BOARD_SIZE), 1);
        assert board2.playMove(6 + (1 * board2.BOARD_SIZE), 2);
        assert board2.playMove(6 + (0 * board2.BOARD_SIZE), 1);
        assert board2.playMove(7 + (0 * board2.BOARD_SIZE), 2);
        assert board2.playMove(5 + (3 * board2.BOARD_SIZE), 1);
        assert board2.playMove(5 + (4 * board2.BOARD_SIZE), 2);

        assert board2.toString().equals(board.toString());
    }
    //   0 1 2 3 4 5 6 7
    // 0 - - - - - - X O
    // 1 - - - - - - O -
    // 2 - - - - X O X -
    // 3 - - - X X O - -
    // 4 - - - O O O - -
    // 5 - - - - - - - -
    // 6 - - - - - - - -
    // 7 - - - - - - - -


    @Test void playExampleMoves2() {
        assert board.playMove(4, 2, 1);
        assert board.playMove(5, 2, 2);
        assert board.playMove(6, 2, 1);
        assert board.playMove(6, 1, 2);
        assert board.playMove(6, 0, 1);
        assert board.playMove(7, 0, 2);
        assert board.playMove(5, 3, 1);
        assert board.playMove(5, 4, 2);
        assert board.playMove(3, 2, 2);
        assert board.playMove(3, 5, 1);
        assert board.playMove(6, 3, 2);
        assert board.playMove(4, 1, 1);
        assert board.playMove(5, 0, 2);
        assert board.playMove(7, 3, 1);
        assert board.playMove(4, 5, 2);
        assert board.playMove(5, 1, 1);
    }
    //   0 1 2 3 4 5 6 7
    // 0 - - - - - O O O
    // 1 - - - - O X O -
    // 2 - - - O O X X -
    // 3 - - - O O X X X
    // 4 - - - O O O - -
    // 5 - - - X O - - -
    // 6 - - - - - - - -
    // 7 - - - - - - - -

    @Test void player2Outplayed() {
        assert board.playMove(4, 2, 1);
        assert board.playMove(3, 2, 2);
        assert board.playMove(2, 2, 1);
        assert board.playMove(5, 2, 2);
        assert board.playMove(6, 2, 1);
        assert board.playMove(4, 5, 2);
        assert board.playMove(4, 6, 1);
        assert board.playMove(5, 4, 2);
        assert board.playMove(3, 5, 1);
        assert board.playMove(4, 1, 2);
        assert board.playMove(4, 0, 1);
        assert board.playMove(2, 4, 2);
        assert board.playMove(6, 5, 1);
        assert board.playMove(3, 6, 2);
        assert board.playMove(1, 4, 1);
        assert board.playMove(5, 5, 2);
        assert board.playMove(2, 5, 1);
        assert board.playMove(5, 6, 2);
        assert board.playMove(4, 7, 1);
        assert board.getGameState() == Reversi.GameState.PLAYER_1_WINS;
    }
    //   0 1 2 3 4 5 6 7
    // 0 - - - - X - - -
    // 1 - - - - X - - -
    // 2 - - X X X X X -
    // 3 - - - X X - - -
    // 4 - X X X X X - -
    // 5 - - X X X X X -
    // 6 - - - X X X - -
    // 7 - - - - X - - -


    @Test
    void playFirstMove() {
        HashMap<Integer, Boolean> legalMoves = new HashMap<>();
        legalMoves.put(24, true);
        legalMoves.put(35, true);
        legalMoves.put(42, true);
        legalMoves.put(53, true);

        for (int y = 0; y < board.BOARD_SIZE; y++) {
            for (int x = 0; x < board.BOARD_SIZE; x++) {
                assert legalMoves.containsKey(Integer.parseInt(""+x+""+y)) == board.playMove(y * board.BOARD_SIZE + x, 1);
                board.reset();
            }
        }
    }
}