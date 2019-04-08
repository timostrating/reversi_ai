package util;

public class Utils {

    public static boolean any(boolean... bools) {
        for (boolean b : bools) if (b) return true;
        return false;
    }

}
