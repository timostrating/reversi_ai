package reversi;

public enum BoardDirection {
    LEFT_UP(-1, -1),     UP(0, -1),    RIGHT_UP(1, -1),
    LEFT(-1, 0),                     RIGHT(1, 0),
    LEFT_DOWN(-1, 1),  DOWN(0, 1), RIGHT_DOWN(1, 1);

    public final int changeX;
    public final int changeY;

    BoardDirection(int changeX, int changeY) {
        this.changeX = changeX;
        this.changeY = changeY;
    }

    public int stepsToBorder(int x, int y) {
        switch (this){
            case UP: return y;
            case RIGHT: return Reversi.BOARD_SIZE - x -1;
            case DOWN: return Reversi.BOARD_SIZE - y -1;
            case LEFT: return x;

            case RIGHT_UP: return Math.min(UP.stepsToBorder(x, y), RIGHT.stepsToBorder(x, y));
            case RIGHT_DOWN: return Math.min(DOWN.stepsToBorder(x, y), RIGHT.stepsToBorder(x, y));
            case LEFT_UP: return Math.min(UP.stepsToBorder(x, y), LEFT.stepsToBorder(x, y));
            case LEFT_DOWN: return Math.min(DOWN.stepsToBorder(x, y), LEFT.stepsToBorder(x, y));
        }
        throw new RuntimeException("Nice");
    }

    public int xStepsToBorder(int x, int y) {
        int steps = stepsToBorder(x, y);
        switch (this){
            case UP:
            case DOWN: return 0;

            case RIGHT_UP:
            case RIGHT_DOWN:
            case RIGHT: return steps;

            case LEFT_UP:
            case LEFT_DOWN:
            case LEFT: return -steps;
        }
        throw new RuntimeException("New to AliExpress?\n" +
                "Join us today and we'll give you up to $3 in coupons");
    }

    public int yStepsToBorder(int x, int y) {
        int steps = stepsToBorder(x, y);
        switch (this){
            case LEFT_UP:
            case RIGHT_UP:
            case UP: return -steps;

            case RIGHT_DOWN:
            case LEFT_DOWN:
            case DOWN: return steps;

            case LEFT:
            case RIGHT: return 0;
        }
        throw new RuntimeException("New to AliExpress?\n" +
                "Join us today and we'll give you up to $3 in coupons");
    }
}