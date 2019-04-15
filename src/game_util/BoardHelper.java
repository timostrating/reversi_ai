package game_util;

/**
 * A small helper that came into live to ease the syntax of the tictactoe implementation
 */
public class BoardHelper {
    public static <T> boolean areAllEqual(T checkValue, T... otherValues) {
        for (T value : otherValues)
            if (value != checkValue)
                return false;

        return true;
    }
}
