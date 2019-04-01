package Util;

public abstract class Player {

    private int nr;

    public final int getNr() {
        return nr;
    }

    final void setNr(int nr) {
        this.nr = nr;
    }

    public abstract int getInput();

}
