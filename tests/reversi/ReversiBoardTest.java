package reversi;

import game_util.DefaultReferee;
import game_util.GameBoard2D;
import game_util.HumanPlayer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

class ReversiBoardTest {

    Reversi reversi = new Reversi();
    GameBoard2D board = reversi.board;

    @BeforeEach
    void BeforeEach() {
        reversi = new Reversi();
        reversi.initialize(new DefaultReferee(reversi), new HumanPlayer(reversi), new HumanPlayer(reversi));
    }

    @AfterEach
    void AfterEach() {
        System.out.println(reversi);
        reversi.reset();
    }

    @Test void playExampleMoves1a() {
        System.err.println(reversi);
        assert reversi.playMove(board.xyToI(3,2), 1);
        System.err.println(reversi);
        assert reversi.playMove(board.xyToI(2,2), 2);
        assert reversi.playMove(board.xyToI(1,2), 1);
        assert reversi.playMove(board.xyToI(1,1), 2);
        assert reversi.playMove(board.xyToI(1,0), 1);
        assert reversi.playMove(board.xyToI(0,0), 2);
        assert reversi.playMove(board.xyToI(2,3), 1);
        assert reversi.playMove(board.xyToI(2,4), 2);
    }
    @Test void playExampleMoves1b() {
        playExampleMoves1a();

        Reversi reversi2 = new Reversi();
        assert reversi2.playMove(3 + (2 * Reversi.BOARD_SIZE), 1);
        assert reversi2.playMove(2 + (2 * Reversi.BOARD_SIZE), 2);
        assert reversi2.playMove(1 + (2 * Reversi.BOARD_SIZE), 1);
        assert reversi2.playMove(1 + (1 * Reversi.BOARD_SIZE), 2);
        assert reversi2.playMove(1 + (0 * Reversi.BOARD_SIZE), 1);
        assert reversi2.playMove(0 + (0 * Reversi.BOARD_SIZE), 2);
        assert reversi2.playMove(2 + (3 * Reversi.BOARD_SIZE), 1);
        assert reversi2.playMove(2 + (4 * Reversi.BOARD_SIZE), 2);

        assert reversi2.toString().equals(reversi.toString());
    }
    //   0 1 2 3 4 5 6 7
    // 0 O X - - - - - -
    // 1 - O - - - - - -
    // 2 - X O X - - - -
    // 3 - - O X X - - -
    // 4 - - O O O - - -
    // 5 - - - - - - - -
    // 6 - - - - - - - -
    // 7 - - - - - - - -


    @Test void playExampleMoves2() {
        assert reversi.playMove(board.xyToI(3,2), 1);
        assert reversi.playMove(board.xyToI(2,2), 2);
        assert reversi.playMove(board.xyToI(1,2), 1);
        assert reversi.playMove(board.xyToI(1,1), 2);
        assert reversi.playMove(board.xyToI(1,0), 1);
        assert reversi.playMove(board.xyToI(0,0), 2);
        assert reversi.playMove(board.xyToI(2,3), 1);
        assert reversi.playMove(board.xyToI(2,4), 2);
        assert reversi.playMove(board.xyToI(4,2), 2);
        assert reversi.playMove(board.xyToI(4,5), 1);
        assert reversi.playMove(board.xyToI(1,3), 2);
        assert reversi.playMove(board.xyToI(3,1), 1);
        assert reversi.playMove(board.xyToI(2,0), 2);
        assert reversi.playMove(board.xyToI(0,3), 1);
        assert reversi.playMove(board.xyToI(3,5), 2);
        assert reversi.playMove(board.xyToI(2,1), 1);
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
        assert reversi.playMove(board.xyToI(3,2), 1);
        assert reversi.playMove(board.xyToI(4,2), 2);
        assert reversi.playMove(board.xyToI(5,2), 1);
        assert reversi.playMove(board.xyToI(2,2), 2);
        assert reversi.playMove(board.xyToI(1,2), 1);
        assert reversi.playMove(board.xyToI(3,5), 2);
        assert reversi.playMove(board.xyToI(3,6), 1);
        assert reversi.playMove(board.xyToI(2,4), 2);
        assert reversi.playMove(board.xyToI(4,5), 1);
        assert reversi.playMove(board.xyToI(3,1), 2);
        assert reversi.playMove(board.xyToI(3,0), 1);
        assert reversi.playMove(board.xyToI(5,4), 2);
        assert reversi.playMove(board.xyToI(1,5), 1);
        assert reversi.playMove(board.xyToI(4,6), 2);
        assert reversi.playMove(board.xyToI(6,4), 1);
        assert reversi.playMove(board.xyToI(2,5), 2);
        assert reversi.playMove(board.xyToI(5,5), 1);
        assert reversi.playMove(board.xyToI(2,6), 2);
        assert reversi.playMove(board.xyToI(3,7), 1);
        assert reversi.getGameState() == Reversi.GameState.PLAYER_1_WINS;
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


    //    @Test
    // TODO: fix, x must be flipped.
    void playFirstMove() {
        HashMap<Integer, Boolean> legalMoves = new HashMap<>();
        legalMoves.put(24, true);
        legalMoves.put(35, true);
        legalMoves.put(42, true);
        legalMoves.put(53, true);

        for (int y = 0; y < reversi.BOARD_SIZE; y++) {
            for (int x = 0; x < reversi.BOARD_SIZE; x++) {
                assert legalMoves.containsKey(Integer.parseInt(""+x+""+y)) == reversi.playMove(y * reversi.BOARD_SIZE + x, 1);
                reversi.reset();
            }
        }
    }


    @Test
    void openPositions() {
        /**
         *   0 1 2 3 4 5 6 7
         * 0 X - X O - - O X
         * 1 - X - O X X X -
         * 2 - X X O X X - -
         * 3 - - - O O X - -
         * 4 - - - O X O - -
         * 5 - - X X - O - -
         * 6 - - X - - - - -
         * 7 - - - - - - - -
         */

        assert reversi.playMove(board.xyToI(2, 3), 1);
        assert reversi.playMove(board.xyToI(2, 2), 2);
        assert reversi.playMove(board.xyToI(2, 1), 1);
        assert reversi.playMove(board.xyToI(1, 1), 2);
        assert reversi.playMove(board.xyToI(3, 2), 1);
        assert reversi.playMove(board.xyToI(4, 2), 2);
        assert reversi.playMove(board.xyToI(0, 0), 1);
        assert reversi.playMove(board.xyToI(2, 5), 2);
        assert reversi.playMove(board.xyToI(5, 2), 1);
        assert reversi.playMove(board.xyToI(1, 0), 2);
        assert reversi.playMove(board.xyToI(3, 1), 1);
        assert reversi.playMove(board.xyToI(6, 1), 2);
        assert reversi.playMove(board.xyToI(5, 5), 1);
        assert reversi.playMove(board.xyToI(4, 5), 2);
        assert reversi.playMove(board.xyToI(6, 2), 1);
        assert reversi.playMove(board.xyToI(4, 1), 2);
        assert reversi.playMove(board.xyToI(5, 0), 1);
        assert reversi.playMove(board.xyToI(2, 4), 2);
        assert reversi.playMove(board.xyToI(7, 0), 1);
        assert reversi.playMove(board.xyToI(4, 0), 2);
        assert reversi.playMove(board.xyToI(5, 6), 1);

//        LinkedList<Integer>
//                openO = reversi.getOpenPositions().openOPositions,
//                openX = reversi.getOpenPositions().openXPositions;
//        assert openO.contains(board.xyToI(6, 0));
//        assert openX.contains(board.xyToI(3, 0));
    }

}