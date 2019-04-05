package util;

public interface OpenPositions {
    int size();

    Integer get(int posIndex);

    Integer remove(int posIndex);

    void add(int posIndex, Integer pos);
}
