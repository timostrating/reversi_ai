package util;

public class OpenPosition {

    public int i;

    public OpenPosition(int i) {
        this.i = i;
    }

    @Override
    public boolean equals(Object o) {
        return ((OpenPosition) o).i == i;
    }

}
