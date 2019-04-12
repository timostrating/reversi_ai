package util;

public class StringUtils {

    public static String capitalize(String s){
        String lowerCased;
        String capitalized;
        lowerCased = s.substring(1).toLowerCase();
        capitalized = s.substring(0,1).toUpperCase();
        capitalized = capitalized + lowerCased;
        return capitalized;
    }
}
