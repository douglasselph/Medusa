package com.dugsolutions.playerand.util;

import java.util.Random;

import timber.log.Timber;

/**
 * Created by dug on 7/11/17.
 */

public class Roll {

    public static void Init() {
        random = new Random();
    }

    public static int Roll(short num, short sides, short add) {
        return new Roll(num, sides, add).roll();
    }

    static Random random;

    public short num;
    public short sides;
    public short add;
    short mult;

    public Roll(int num, int sides, int add) {
        this.num = (short) num;
        this.add = (short) add;
        if (sides < 0) {
            this.sides = (short) -sides;
            mult = -1;
        } else {
            this.sides = (short) sides;
            mult = 1;
        }

    }

    public Roll(String expr) {
        if (expr == null) {
            return;
        }
        int startPos = 0;
        if (expr.charAt(0) == '-') {
            mult = -1;
            startPos = 1;
        } else {
            mult = 1;
        }
        int dPos = expr.toLowerCase().indexOf('d');
        if (dPos < 0) {
            Timber.e("Invalid expression for roll: " + expr);
        } else {
            int pPos = expr.indexOf('+');
            num = (short) Integer.parseInt(expr.substring(startPos, dPos));
            if (pPos >= 0) {
                sides = (short) Integer.parseInt(expr.substring(dPos + 1, pPos));
                add = (short) Integer.parseInt(expr.substring(pPos + 1));
            } else {
                sides = (short) Integer.parseInt(expr.substring(dPos + 1));
            }
        }
    }

    public short roll() {
        short total = add;
        for (int i = 0; i < num; i++) {
            total += (random.nextInt(sides) + 1) * mult;
        }
        return total;
    }

    public String toString() {
        StringBuilder sbuf = new StringBuilder();
        if (mult < 0) {
            sbuf.append("-");
        }
        sbuf.append(num);
        sbuf.append("d");
        sbuf.append(sides);
        if (add > 0) {
            sbuf.append("+");
            sbuf.append(add);
        }
        return sbuf.toString();
    }
}
