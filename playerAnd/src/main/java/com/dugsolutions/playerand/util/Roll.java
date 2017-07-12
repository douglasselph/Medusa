package com.dugsolutions.playerand.util;

import java.util.Random;

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

    public short roll() {
        short total = add;
        for (int i = 0; i < num; i++) {
            total += (random.nextInt(sides) + 1) * mult;
        }
        return total;
    }
}
