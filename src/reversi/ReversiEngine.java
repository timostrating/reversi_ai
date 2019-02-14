package reversi;

public class ReversiEngine {

    public static final int Black = 1;
    public static final int White = 2;
    public static final int Empty = 0;

    ReversiBoard board;

    public ReversiEngine() {
        board = new ReversiBoard(8);
    }

    public static void main(String[] args) {
        new ReversiEngine();
    }
}
