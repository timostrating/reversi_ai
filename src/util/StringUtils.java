package util;

public class StringUtils {

    public static String capitalize(String s){
        String lowerCased;
        String capitalized;
        lowerCased = s.substring(1);
        capitalized = s.substring(0,1);
        capitalized = capitalized.toUpperCase() + lowerCased;
        return capitalized;
    }
}
