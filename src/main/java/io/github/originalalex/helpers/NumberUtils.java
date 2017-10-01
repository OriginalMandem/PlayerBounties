package io.github.originalalex.helpers;

public class NumberUtils {

    public static double convert(String str) {
        try {
            return Double.valueOf(str);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

}
