package toernooi;

import game_util.DefaultReferee;
import game_util.HumanPlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reversi.Reversi;

public class TroernooiData {

    Reversi reversi = new Reversi();

    @BeforeEach
    void BeforeEach() {
        reversi = new Reversi();
        reversi.initialize(new DefaultReferee(reversi), new HumanPlayer(reversi), new HumanPlayer(reversi));
    }

    @Test
    void playGame1() {  // we are 2 we WON
        System.out.print(reversi);
        assert reversi.playMove(37, 1);
        System.out.print(reversi);
        assert reversi.playMove(45, 2);
        System.out.print(reversi);
        assert reversi.playMove(53, 1);
        System.out.print(reversi);
        assert reversi.playMove(29, 2);
        System.out.print(reversi);
        assert reversi.playMove(21, 1);
        System.out.print(reversi);
        assert reversi.playMove(38, 2);
        assert reversi.playMove(39, 1);    assert reversi.playMove(43, 2);
        assert reversi.playMove(50, 1);    assert reversi.playMove(51, 2);
        assert reversi.playMove(18, 1);    assert reversi.playMove(14, 2);
        assert reversi.playMove(59, 1);    assert reversi.playMove(42, 2);
        assert reversi.playMove(34, 1);    assert reversi.playMove(46, 2);
        assert reversi.playMove( 7, 1);    assert reversi.playMove(61, 2);
        assert reversi.playMove(55, 1);    assert reversi.playMove(13, 2);
        assert reversi.playMove(60, 1);    assert reversi.playMove(58, 2);
        assert reversi.playMove( 5, 1);    assert reversi.playMove(26, 2);
        assert reversi.playMove(33, 1);    assert reversi.playMove(44, 2);
        assert reversi.playMove(17, 1);    assert reversi.playMove(10, 2);
        assert reversi.playMove(57, 1);    assert reversi.playMove(16, 2);
        assert reversi.playMove(62, 1);    assert reversi.playMove(52, 2);
        assert reversi.playMove( 2, 1);    assert reversi.playMove(47, 2);
        assert reversi.playMove(54, 1);    assert reversi.playMove(63, 2);
        assert reversi.playMove( 8, 1);    assert reversi.playMove(56, 2);
        assert reversi.playMove(24, 1);    assert reversi.playMove(25, 2);
        assert reversi.playMove(32, 1);    assert reversi.playMove(31, 2);
        assert reversi.playMove(30, 1);    assert reversi.playMove(49, 2);
        assert reversi.playMove(48, 1);    assert reversi.playMove(40, 2);
        assert reversi.playMove(41, 1);    assert reversi.playMove(19, 2);
        assert reversi.playMove(20, 1);    assert reversi.playMove(0, 2);     assert reversi.playMove(1, 2);
        assert reversi.playMove(11, 1);    assert reversi.playMove(4, 2);
        assert reversi.playMove( 3, 1);    assert reversi.playMove(9, 2);     assert reversi.playMove(23, 2);
        assert reversi.playMove(22, 1);    assert reversi.playMove(12, 2);    assert reversi.playMove(6, 2);
        assert reversi.playMove(15, 1);

        /*
              0 1 2 3 4 5 6 7
            0 O O O O O O O X
            1 O O O O O O O X
            2 O O O O O O X O
            3 O O O X O X O O
            4 O O X O X O X O
            5 O X X O O O O O
            6 O O X X X X X O
            7 O O O O O O O O

            SVR GAME WIN {PLAYERONESCORE: "15", PLAYERTWOSCORE: "49", COMMENT: ""}
         */
    }


    @Test
    void playGame2() {  // we are 1 we LOST
        assert reversi.playMove(19, 1);    assert reversi.playMove(18, 2);
        assert reversi.playMove(37, 1);    assert reversi.playMove(20, 2);
        assert reversi.playMove(26, 1);    assert reversi.playMove(34, 2);
        assert reversi.playMove(17, 1);    assert reversi.playMove(16, 2);
        assert reversi.playMove(10, 1);    assert reversi.playMove(2, 2);
        assert reversi.playMove(21, 1);    assert reversi.playMove(46, 2);
        assert reversi.playMove(38, 1);    assert reversi.playMove(42, 2);
        assert reversi.playMove(55, 1);    assert reversi.playMove(39, 2);
        assert reversi.playMove(1, 1);     assert reversi.playMove(0, 2);
        assert reversi.playMove(44, 1);    assert reversi.playMove(11, 2);
        assert reversi.playMove(12, 1);    assert reversi.playMove(29, 2);
        assert reversi.playMove(33, 1);    assert reversi.playMove(3, 2);
        assert reversi.playMove(50, 1);    assert reversi.playMove(4, 2);
        assert reversi.playMove(22, 1);    assert reversi.playMove(23, 2);
        assert reversi.playMove(5, 1);     assert reversi.playMove(45, 2);
        assert reversi.playMove(14, 1);    assert reversi.playMove(47, 2);
        assert reversi.playMove(53, 1);    assert reversi.playMove(43, 2);
        assert reversi.playMove(52, 1);    assert reversi.playMove(13, 2);
        assert reversi.playMove(31, 1);    assert reversi.playMove(63, 2);
        assert reversi.playMove(8, 1);     assert reversi.playMove(7, 2);
        assert reversi.playMove(30, 1);    assert reversi.playMove(54, 2);
        assert reversi.playMove(15, 1);    assert reversi.playMove(6, 2);
        assert reversi.playMove(9, 1);     assert reversi.playMove(24, 2);
        assert reversi.playMove(25, 1);    assert reversi.playMove(40, 2);
        assert reversi.playMove(41, 1);    assert reversi.playMove(32, 2);
        assert reversi.playMove(62, 1);    assert reversi.playMove(58, 2);
        assert reversi.playMove(59, 1);    assert reversi.playMove(61, 2);
        assert reversi.playMove(49, 1);    assert reversi.playMove(60, 2);
        assert reversi.playMove(48, 1);    assert reversi.playMove(56, 2);
        assert reversi.playMove(57, 1);    assert reversi.playMove(51, 2);

        /*
              0 1 2 3 4 5 6 7
            0 O O O O O O O O
            1 O X O O O X X X
            2 O X O O O O X O
            3 O O O O O X O O
            4 O O O O O O X O
            5 O X O O O O X O
            6 O O O O O O O O
            7 O X O O O O O O

            SVR GAME LOSS {PLAYERONESCORE: "11", PLAYERTWOSCORE: "53", COMMENT: ""}
         */
    }

    @Test
    void playGame3() {  // we are 1
        assert reversi.playMove(19, 1);    assert reversi.playMove(20, 2);
        assert reversi.playMove(21, 1);    assert reversi.playMove(34, 2);
        assert reversi.playMove(45, 1);    assert reversi.playMove(14, 2);
        assert reversi.playMove(29, 1);    assert reversi.playMove(13, 2);
        assert reversi.playMove(7, 1);     assert reversi.playMove(54, 2);
        assert reversi.playMove(5, 1);     assert reversi.playMove(22, 2);
        assert reversi.playMove(37, 1);    assert reversi.playMove(6, 2);
        assert reversi.playMove(12, 1);    assert reversi.playMove(30, 2);
        assert reversi.playMove(33, 1);    assert reversi.playMove(3, 2);
        assert reversi.playMove(4, 1);     assert reversi.playMove(11, 2);
        assert reversi.playMove(63, 1);    assert reversi.playMove(53, 2);
        assert reversi.playMove(23, 1);    assert reversi.playMove(43, 2);
        assert reversi.playMove(42, 1);    assert reversi.playMove(15, 2);
        assert reversi.playMove(51, 1);    assert reversi.playMove(59, 2);
        assert reversi.playMove(61, 1);    assert reversi.playMove(31, 2);
        assert reversi.playMove(44, 1);    assert reversi.playMove(50, 2);
        assert reversi.playMove(58, 1);    assert reversi.playMove(49, 2);
        assert reversi.playMove(56, 1);    assert reversi.playMove(46, 2);
        assert reversi.playMove(47, 1);    assert reversi.playMove(41, 2);
        assert reversi.playMove(40, 1);    assert reversi.playMove(32, 2);
        assert reversi.playMove(60, 1);    assert reversi.playMove(52, 2);
        assert reversi.playMove(2, 1);     assert reversi.playMove(38, 2);
        assert reversi.playMove(18, 1);    assert reversi.playMove(26, 2);
        assert reversi.playMove(57, 1);    assert reversi.playMove(55, 2);
        assert reversi.playMove(39, 1);    assert reversi.playMove(62, 2);
        assert reversi.playMove(48, 1);    assert reversi.playMove(9, 2);
        assert reversi.playMove(10, 1);    assert reversi.playMove(1, 2);
        assert reversi.playMove(24, 1);    assert reversi.playMove(25, 2);
        assert reversi.playMove(16, 1);    assert reversi.playMove(17, 1);    assert reversi.playMove(8, 2);
        assert reversi.playMove(0, 1);
    }

    /*
          0 1 2 3 4 5 6 7
        0 X X X X X X X X
        1 X X O O O O O X
        2 X O X X X X O X
        3 X X O O O O X X
        4 X X X O O O X X
        5 X X X X O O O X
        6 X X X O X O O O
        7 X X X X X X O X

        SVR GAME WIN {PLAYERONESCORE: "42", PLAYERTWOSCORE: "22", COMMENT: ""}
     */
}
