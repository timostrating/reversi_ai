package reversi;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class ReversiBoardTest {

    ReversiBoard board = new ReversiBoard(8);

    @BeforeEach
    void BeforeEach() {
        board = new ReversiBoard(8);
    }

    @AfterEach
    void AfterEach() {
        System.out.println(board);
    }

    @Test
    void playFirstMove() {
        ArrayList legalMoves = new ArrayList<int[]>();
        legalMoves.add(new int[]{2, 4});
        legalMoves.add(new int[]{3, 4});
        legalMoves.add(new int[]{4, 2});
        legalMoves.add(new int[]{3, 4});

        for (int y = 0; y < board.getBoardSize(); y++)
            for (int x = 0; x < board.getBoardSize(); x++)
                assert (legalMoves.contains(new int[]{x, y}) == board.playMove(x, y, 1));
    }
}