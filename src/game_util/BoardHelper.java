package game_util;

public class BoardHelper {
    public static <T> boolean areAllEqual(T checkValue, T... otherValues) {
        for (T value : otherValues)
            if (value != checkValue)
                return false;

        return true;
    }
}
