package com.fomono.fomono.utils;

/**
 * Created by David on 4/9/2017.
 */

public class NumberUtil {

    public static int convertToMeters(int miles) {
        double result = miles * 1609.344;
        return (int) result;
    }
}
