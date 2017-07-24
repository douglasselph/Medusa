package com.dugsolutions.playerand.util;

import java.util.Random;

/**
 * Created by dug on 7/11/17.
 */

public class RollGroup {

    Roll[] group;

    public RollGroup(Roll ... args) {
        group = new Roll[args.length];
        int i = 0;
        for (Roll roll : args) {
            group[i++] = roll;
        }
    }

    public short roll() {
        short total = 0;
        for (Roll roll : group) {
            total += roll.roll();
        }
        return total;
    }

    public String toString() {
        StringBuilder sbuf = new StringBuilder();
        for (Roll roll : group) {
            if (sbuf.length() > 0) {
                sbuf.append("+");
            }
            sbuf.append(roll.toString());
        }
        return sbuf.toString();
    }
}
