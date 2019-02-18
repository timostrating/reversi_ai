package reversi;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

class ReversiBoardTest {

    ReversiBoard board = new ReversiBoard(8);

    @BeforeEach
    void BeforeEach() {
        board = new ReversiBoard(8);
    }

    @AfterEach
    void AfterEach() {
        System.out.println(board);
        board.reset();
    }

    @Test void playExampleMoves1() {
        assert board.playMove(4, 2, 1);
        assert board.playMove(5, 2, 2);
        assert board.playMove(6, 2, 1);
        assert board.playMove(6, 1, 2);
        assert board.playMove(6, 0, 1);
        assert board.playMove(7, 0, 2);
        assert board.playMove(5, 3, 1);
        assert board.playMove(5, 4, 2);
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


    @Test
    void playFirstMove() {
        HashMap<Integer, Boolean> legalMoves = new HashMap<>();
        legalMoves.put(24, true);
        legalMoves.put(35, true);
        legalMoves.put(42, true);
        legalMoves.put(53, true);

        for (int y = 0; y < board.getBoardSize(); y++) {
            for (int x = 0; x < board.getBoardSize(); x++) {
                assert legalMoves.containsKey(Integer.parseInt(""+x+""+y)) == board.playMove(x, y, 1);
                board.reset();
            }
        }
    }
}