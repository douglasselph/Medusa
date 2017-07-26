package com.dugsolutions.playerand.util;

/**
 * Created by dug on 7/25/17.
 */

public class StringUtils {

    // Return the string with each word capitalized.
    public static String capitalize(String value) {
        StringBuilder sbuf = new StringBuilder();
        value = value.trim();
        int pos = 0;
        int spos;
        while (pos < value.length()) {
            sbuf.append(value.substring(pos, pos+1).toUpperCase());
            pos++;
            spos = value.indexOf(' ', pos);
            if (spos == -1) {
                sbuf.append(value.substring(pos).toLowerCase());
                break;
            }
            spos++;
            sbuf.append(value.substring(pos, spos).toLowerCase());
            pos = spos;
        }
        return sbuf.toString();
    }
}
