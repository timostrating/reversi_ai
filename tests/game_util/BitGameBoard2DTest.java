package game_util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reversi.Reversi;

class BitGameBoard2DTest {

    BitGameBoard2D board;

    @BeforeEach
    void BeforeAll() {
        board = new BitGameBoard2D(8);
    }

    @Test
    void get() {
        board.set(19, 1);
        assert board.get(19) == 1;
        System.out.println(board);
    }

    @Test
    void reversiReset() {
        Reversi reversi = new Reversi();
        reversi.reset();
        System.out.print(reversi.board);
    }


    @Test
    void setZero() {
        board.set(19, 1);
        board.set(19, 2);

//        System.out.println(board.toBinaryString());
        assert board.get(19) == 2;

        board.set(20, 2);
        board.set(20, 1);

//        System.out.println(board.toBinaryString());
        assert board.get(20) == 1;

        board.set(19, 0);
        board.set(20, 0);

//        System.out.println(board.toBinaryString());
        assert board.get(19) == 0;
        assert board.get(20) == 0;
    }
}