package util;

public interface OpenPositions<T extends OpenPosition> {

    int size(int playerNr);

    T get(int posIndex, int playerNr);

    T remove(int posIndex, int playerNr);

    void add(int posIndex, T pos, int playerNr);

    default void filter(int i, int playerNr) {
        for (int j = 0; j < size(playerNr); j++) if (get(j, playerNr ).i == i) {
            remove(j, playerNr);
            return;
        }
    }
}
