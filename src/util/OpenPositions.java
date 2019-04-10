package util;

public interface OpenPositions {

    int size(int playerNr);

    int get(int posIndex, int playerNr);

    int remove(int posIndex, int playerNr);

    void add(int posIndex, int pos, int playerNr);

    default void filter(int i, int playerNr) {
        for (int j = 0; j < size(playerNr); j++) if (get(j, playerNr ) == i) {
            remove(j, playerNr);
            return;
        }
    }

    default boolean contains(int i, int playerNr) {
        for (int j = 0; j < size(playerNr); j++) if (get(j, playerNr ) == i) return true;
        return false;
    }

}
