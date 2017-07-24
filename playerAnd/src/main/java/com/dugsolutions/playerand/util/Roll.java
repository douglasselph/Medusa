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

    public static Random getRandom() {
        return random;
    }

    static Random random;

    public short num;
    public short sides;
    public short add;
    short mult;

    public Roll(Roll other) {
        num = other.num;
        sides = other.sides;
        add = other.add;
        mult = other.mult;
    }

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

    /**
     * Forms handled:
     * -1d6
     * 3d6
     * 3d6+1
     * 3d6*75
     * 3d6*75+1
     *
     * @param expr
     */
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
        int dPos = expr.indexOf('d');
        if (dPos < 0) {
            dPos = expr.indexOf('D');
            if (dPos < 0) {
                Timber.e("Invalid expression for roll: " + expr);
                return;
            }
        }
        num = (short) Integer.parseInt(expr.substring(startPos, dPos));
        int pPos = expr.indexOf('+');
        int sPos = expr.indexOf('*');
        if (pPos > 0 && sPos > 0) {
            if (sPos > pPos) {
                Timber.e("Invalid expression for roll: " + expr);
                return;
            }
            sides = (short) Integer.parseInt(expr.substring(dPos + 1, sPos));
            mult *= (short) Integer.parseInt(expr.substring(sPos + 1, pPos));
            add = (short) Integer.parseInt(expr.substring(pPos + 1));
        } else if (pPos > 0) {
            sides = (short) Integer.parseInt(expr.substring(dPos + 1, pPos));
            add = (short) Integer.parseInt(expr.substring(pPos + 1));
        } else if (sPos > 0) {
            sides = (short) Integer.parseInt(expr.substring(dPos + 1, sPos));
            mult *= (short) Integer.parseInt(expr.substring(sPos + 1));
            add = 0;
        } else {
            sides = (short) Integer.parseInt(expr.substring(dPos + 1));
            add = 0;
        }
    }

    public short roll() {
        short total = add;
        for (int i = 0; i < num; i++) {
            total += (random.nextInt(sides) + 1);
        }
        total *= mult;
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

    public int getMin() {
        int total = add;
        total += num;
        total *= mult;
        return total;
    }

    public int getMax() {
        int total = add;
        total += num * sides;
        total *= mult;
        return total;
    }
}
