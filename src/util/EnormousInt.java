package util;

import java.math.BigInteger;

/**
 * Big integer does not have a constructor that takes in an int
 */
public class EnormousInt extends BigInteger {

    public static final BigInteger MAX_PRIME_LESS_THAN_INTEGER_MAXVALUE = new EnormousInt(2147483629);

    public EnormousInt(int s) {
        super(""+s);
    }
}