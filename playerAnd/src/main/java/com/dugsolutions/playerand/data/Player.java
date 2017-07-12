package com.dugsolutions.playerand.data;

/**
 * Created by dug on 7/12/17.
 */

public class Player extends Animal {

    short height;

    public Player(RaceCreature race) {
        super(race);
    }

    public int getExperienceMod() {
        int value = (int) Math.ceil(cha / 6) - 2;
        if (value < -1) {
            return -1;
        }
        return value;
    }

    public int getHealingRate() {
        return (int) Math.ceil(con / 6);
    }

    public int getHeight() {
        return height;
    }
}
