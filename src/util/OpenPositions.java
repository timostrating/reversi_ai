package util;

public interface OpenPositions {
    int size();

    Integer get(int posIndex);

    Integer remove(int posIndex);
    boolean remove(Object pos);

    boolean add(Integer pos);
    void add(int posIndex, Integer pos);
}
