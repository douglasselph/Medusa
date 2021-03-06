package com.dugsolutions.playerand.util;

/**
 * Created by dug on 7/12/17.
 */

public class Range {
    public short min;
    public short max;

    public Range(int min, int max) {
        this.min = (short) min;
        this.max = (short) max;
    }

    public int roll() {
        int num = max - min + 1;
        return Roll.getRandom().nextInt(num) + min;
    }
}
