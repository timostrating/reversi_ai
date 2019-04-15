package game_util;

/**
 * The Game board implemented as 2 longs
 */
public class BitGameBoard2D implements GameBoard {

    long boardX;
    long boardO;
    int boardSize;

    public BitGameBoard2D(int boardSize) {
        this.boardSize = boardSize;
    }

    @Override
    public int getBoardSize() {
        return boardSize;
    }

    @Override
    public BitGameBoard2D clone() {
        BitGameBoard2D clone = new BitGameBoard2D(boardSize);
        clone.setMasks(boardX, boardO);
        return clone;
    }

    @Override
    public void reset() {
        boardX = 0;
        boardO = 0;
    }

    public void setMasks(long maskX, long maskO) {
        boardX = maskX;
        boardO = maskO;
    }

    public void setMasks(long mask, int playerNr) {
        if (playerNr == 1){
            boardX = mask;
        } else { // player 2
            boardO = mask;
        }
    }

    @Override
    public int get(int i) {
        long bla = 1L << i;     // L for Long
        if ((boardX & bla) == bla)
            return 1;

        if ((boardO & bla) == bla)
            return 2;

        return 0;
    }

    @Override
    public void set(int i, int v) {
        long mask = 1L << i;     // L for Long
        if (v == 0) {
            boardX &= ~mask;
            boardO &= ~mask;
        } else if (v == 1) {
            boardX |= mask;
            boardO &= ~mask;
        } else { // v = 2
            boardO |= mask;
            boardX &= ~mask;
        }
    }

    public String toBinaryString() {
        StringBuilder sb = new StringBuilder();

        sb.append("BoardX: ");
        for(long i=0; i<64; i++) {
            long bla = 1L << i;
            sb.append(((boardX & bla) == bla) ? "1" : "0");
        }

        sb.append("\nBoardO: ");
        for(long i=0; i<64; i++) {
            long bla = 1L << i;
            sb.append(((boardO & bla) == bla) ? "1" : "0");
        }

        return sb.append("\n").toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int y = 0; y < boardSize; y++) {
            for (int x = 0; x < boardSize; x++)
                sb.append(get(y * boardSize + x)).append(" ");
            sb.append("\n");
        }

        return new String(sb);
    }
}