package util;

public class ArrayUtils {

    public static <T> boolean contains(T[] arr, T obj) {
        for (T o : arr)
            if (o == obj || o.equals(obj))
                return true;

        return false;
    }

}
